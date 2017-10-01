package com.ef.service;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ef.annotation.DirectiveHint;
import com.ef.vo.DirectiveVO;
import com.ef.vo.ParamVO;
import com.ef.vo.RegexVO;

public class ParserService {
	
	public static final String DEFAULT_SEPARATOR = " ";
	public static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");

	private static final String ARG_STARTDATE = "--startDate";
	private static final String ARG_DURATION = "--duration";
	private static final String ARG_THRESHOLD = "--threshold";
	
	// properties
	private Map<String, String> dir2regex;
	private String separator = DEFAULT_SEPARATOR;
	private String logformat; // apache/httpd log format
	
	@Autowired
	@Qualifier("argDateFormatter")
	private SimpleDateFormat dateFormatter;
	
	/**
	 * Utility for escaping regex special character, like "|" (pipe character)
	 * @param str
	 * @return
	 */
	public static String escapeSpecialRegexChars(String str) {
	    return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");
	}
	
	/**
	 * Compute for the corresponding regex expression of (HTTPD's) LogFormat pattern.
	 * @return
	 */
	public RegexVO computeRegEx() {
		
		StringTokenizer tokenizer = new StringTokenizer(logformat, separator);
		
		List<DirectiveVO> directives = new ArrayList<>();
		
		// look for logfile directives
		registerDirectives(tokenizer, directives);
		
		// compute corresponding regex
		directiveToRegex(directives);
		
		// print regex
		StringBuilder sb = new StringBuilder("^");
		String sep = "";
		for(DirectiveVO directive : directives) {
			sb.append(escapeSpecialRegexChars(sep));
			
			if(directive.getWrapper() != null) {
				sb.append(directive.getWrapper());
			}
			
			sb.append(directive.getRegex());

			if(directive.getWrapper() != null) {
				sb.append(directive.getWrapper());
			}
			
			sep = separator;
		}
		
		// output
		RegexVO vo = new RegexVO();
		vo.setLogformat(logformat);
		vo.setRegex(sb.toString());
		vo.setDirectives(directives);
		
		return vo;
	}

	/**
	 * Create directive object for each token.
	 * 
	 * @param tokenizer
	 * @param directives
	 */
	private void registerDirectives(StringTokenizer tokenizer, List<DirectiveVO> directives) {

		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			DirectiveVO directive = new DirectiveVO();
			int len = token.length();
			
			if(token.startsWith("%")) {
				directive.setDirective(token.substring(len - 1));
			} else if(token.startsWith("\\\"%") && token.endsWith("\\\"")) {
				directive.setDirective(token.substring(len-3, len-2));
				directive.setWrapper("\\\"");
			} else {
				directive.setRaw(token);
			}
			
			directives.add(directive);
		}
	}
	
	/**
	 * Define regex expression for each log format directive.
	 * @param directives
	 */
	private void directiveToRegex(List<DirectiveVO> directives) {

		for(DirectiveVO directive : directives) {
			String direc = directive.getDirective();
			if(direc != null) {
				// look for custom mapping
				if(dir2regex != null && dir2regex.get(direc) != null) {
					directive.setRegex(dir2regex.get(direc));
				} else {
					// use default regex expression
					directive.setRegex("(.+)");
				}
			} else {
				// use raw
				directive.setRegex(directive.getRaw());
			}
		}
	}

	/**
	 * Check if the computed pattern matches the given log line.
	 * @param pattern
	 * @param logline
	 * @return
	 */
	public Matcher findMatch(String pattern, String logline) {
		Pattern r = Pattern.compile(pattern);
		
		Matcher m = r.matcher(logline);
		
        return m.find() ? m : null;
	}
	
	/**
	 * Create a POJO instance mapped from the web access log detail.
	 * @param regexVo
	 * @param matcher
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	public <T> T mapToPojo(RegexVO regexVo, Matcher matcher, Class<T> type) throws ServiceException {
		
		T pojo = null;
		try {
			pojo = type.newInstance();
		} catch(Exception e) {
			throw new ServiceException("Unable to create instance: " + type, e);
		}
		
		for(Field field : type.getDeclaredFields()) {
			
			DirectiveHint hint  = field.getAnnotation(DirectiveHint.class);
			if(hint != null) {
				field.setAccessible(true);
				
				int idx = findIndex(hint.value(), regexVo.getDirectives());
				if(idx >= 0) {
					String value = matcher.group(idx + 1);
					try {
						field.set(pojo, value);
					} catch(Exception e) {
						throw new ServiceException("Unable to assign propery: " + field.getName() + " = " + value, e);
					}
				}
			}
		}
		
		
		return pojo;
	}

	/**
	 * Utility for finding the list index of the directive's place-holder item.
	 * @param value
	 * @param directives
	 * @return
	 */
	private int findIndex(String value, List<DirectiveVO> directives) {
		
		// pre-condition check
		if(directives == null) {
			return -1;
		}
		
		int size = directives.size();
		
		for(int k = 0; k < size; k++) {
			DirectiveVO vo = directives.get(k);
			
			if(value.equals(vo.getDirective())) {
				return k;
			}
			
		}
		
		return -1; // not-found
		
	}
	
	/**
	 * Utility for returning the command option usage string.
	 * @param arg
	 * @return
	 */
	private String usage(String arg) {
		
		switch(arg) {
		case ARG_STARTDATE:
			return "The " + arg + " parameter value must be of '" + this.dateFormatter.toPattern() + "' format.";
		case ARG_DURATION:
			return "The " + arg + " parameter value can only be 'hourly' or 'daily'.";
		case ARG_THRESHOLD:
			return "The " + arg + " parameter value must be of positive integer type";
		default:
			return "Unknown argument: " + arg;
		}
		
	}
	
	/**
	 * Evaluate the program's command parameters.
	 * @param args
	 * @return
	 * @throws ServiceException
	 */
	public ParamVO getParameters(String ... args) throws ServiceException {
		ParamVO param = new ParamVO();
		
		int len = args.length;
		for(int k = 0; k < len - 1; k++) {
			String arg = args[k];
			if(ARG_STARTDATE.equals(arg)) {
				assignStartDate(param, arg, args[k+1]);
				
			} else if(ARG_DURATION.equals(arg)) {
				assignDuration(param, arg, args[k+1]);
				
				
			} else if(ARG_THRESHOLD.equals(arg)) {
				assignThreshold(param, arg, args[k+1]);
			}
		}
		
		// post-process
		validateParam(param);
		
		return param;
	}

	/**
	 * Save startDate option to a param object
	 * @param param
	 * @param arg
	 * @param startDate
	 * @throws ServiceException
	 */
	private void assignStartDate(ParamVO param, String arg, String startDate) throws ServiceException {

		try {
			Date date = dateFormatter.parse(startDate);
			param.setStartDate(date);
		} catch (ParseException e) {
			throw new ServiceException(usage(arg));
		}
	}

	/**
	 * Save duration option to a param object
	 * @param param
	 * @param arg
	 * @param duration
	 * @throws ServiceException
	 */
	private void assignDuration(ParamVO param, String arg, String duration) throws ServiceException {
		
		if("hourly".equalsIgnoreCase(duration) || "daily".equalsIgnoreCase(duration)) {
			param.setDuration(duration);
		} else {
			throw new ServiceException(usage(arg));
		}
	}

	/**
	 * Save threshold option to a param object
	 * @param param
	 * @param arg
	 * @param threshold
	 * @throws ServiceException
	 */
	private void assignThreshold(ParamVO param, String arg, String threshold) throws ServiceException {
		
		int value = 0;
		try {
			value = Integer.parseInt(threshold);
		} catch (NumberFormatException e) {
			value = -1;
		}
		
		if(value > 0) {
			param.setThreshold(value);
		} else {
			throw new ServiceException(usage(arg));
		}
	}

	/**
	 * All parameter object properties are mandatory.
	 * 
	 * @param param
	 * @throws ServiceException
	 */
	private void validateParam(ParamVO param) throws ServiceException {

		StringBuilder sb = new StringBuilder();
		if(param.getStartDate() == null) {
			sb.append(ARG_STARTDATE).append(" ");
		} 
		if(param.getDuration() == null) {
			sb.append(ARG_DURATION).append(" ");
		}
		if(param.getThreshold() == 0) {
			sb.append(ARG_THRESHOLD);
		}
		
		if(sb.length() > 0) {
			throw new ServiceException("Missing argument/s: " + sb.toString());
		}
	}

	/**
	 * Computer for end date based on duration.
	 * 
	 * @param param
	 * @return
	 */
	public Date computeEndDate(ParamVO param) {
		
		// pre-condition check
		if(param == null || param.getStartDate() == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(param.getStartDate());
		
		// evaluate duration
		String duration =  param.getDuration();
		if("daily".equalsIgnoreCase(duration)) {
			calendar.add(Calendar.HOUR_OF_DAY, 24);
		} else if("hourly".equalsIgnoreCase(duration)) {
			calendar.add(Calendar.HOUR_OF_DAY, 1);
		}
		
		return calendar.getTime();
	}

	/**
	 * @return the dir2regex
	 */
	public Map<String, String> getDir2regex() {
		return dir2regex;
	}

	/**
	 * @param dir2regex the dir2regex to set
	 */
	public void setDir2regex(Map<String, String> dir2regex) {
		this.dir2regex = dir2regex;
	}

	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * @return the logformat
	 */
	public String getLogformat() {
		return logformat;
	}

	/**
	 * @param logformat the logformat to set
	 */
	public void setLogformat(String logformat) {
		this.logformat = logformat;
	}


}
