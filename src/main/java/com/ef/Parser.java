/**
 * 
 */
package com.ef;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ef.orm.model.AccessSummary;
import com.ef.orm.model.IpCountAggregateResult;
import com.ef.orm.model.ParseJob;
import com.ef.orm.service.DataAccessService;
import com.ef.reader.InputReader;
import com.ef.service.ParserService;
import com.ef.service.ServiceException;
import com.ef.vo.LogDetailVO;
import com.ef.vo.ParamVO;
import com.ef.vo.RegexVO;

/**
 * The main application module.
 * 
 * @author owenrb
 *
 */
public class Parser {

	private static final Logger LOG = Logger.getLogger(Parser.class);
	
	@Autowired
	private InputReader inputReader;
	@Autowired
	private ParserService parserService;
	@Autowired
	private DataAccessService dataService;
	
	@Autowired
	@Qualifier("reason")
	private Properties env;
	
	/**
	 * The application's entry point.
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		try(ClassPathXmlApplicationContext context = 
				new ClassPathXmlApplicationContext("Spring-Module.xml")) {
		
			Parser parser = context.getBean(Parser.class);
			
			// write all logs to DB first
			parser.write();
			
			// retrieve DB entries based on command arguments.
			parser.read(args);
		}
		
	}

	/**
	 * Main routine for retrieving log info and saving them to the database.
	 * 
	 * @param context
	 * @throws IOException
	 */
	private void write() throws IOException {

		String logFormat = null;
		RegexVO regex = null;
		
		
		regex = parserService.computeRegEx();
		
		LOG.debug("logformat: " + logFormat);
		LOG.debug("regex: " + regex.getRegex());
		
		// start reading from pre-defined input stream
		try(BufferedReader br = inputReader.createBufferedReader()) {
			String line=null;
			int lineNum = 0;
			
			// for each line...
	        while( (line=br.readLine()) != null) {
	        		lineNum++;
	        		
	        		// check for matching pattern
	        		Matcher match = parserService.findMatch(regex.getRegex(), line);
	        		if(match != null) {
	        			try {
	        				// map the log info to POJO instance
						LogDetailVO pojo = parserService.mapToPojo(regex, match, LogDetailVO.class);
						
						// persist log entry in DB.
						dataService.add(pojo);
						
					} catch (ServiceException e) {
						LOG.error("[" + lineNum + "] : " + line, e);
					}
	        		} else {
	        			LOG.warn("NOT MATCHED [" + lineNum + "] : " + line);
	        		}
	        		
	        }
		}
        
	}

	/**
	 * Main routine for retrieving entries from the database.
	 * 
	 * @param args
	 */
	private void read(String[] args) {
		
		// read input parameters
		ParamVO param = null;
		try {
			param = parserService.getParameters(args);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			return;
		}
		
		Date endDate = parserService.computeEndDate(param);
		
		// register job
		ParseJob job = new ParseJob();
		job.setJobExecutionDate(new Date());
		job.setStartDateParam(param.getStartDate());
		job.setDurationParam(param.getDuration());
		job.setThresholdParam(param.getThreshold());
		dataService.add(job);
		
		// threshold check
		List<IpCountAggregateResult> resultSet = dataService.searchLogDetail(param.getStartDate(), endDate, param.getThreshold());
		
		for(IpCountAggregateResult result : resultSet) {
			LOG.info("");
			LOG.info("IP: " + result.getIp());
			LOG.info("TOTAL COUNT: " + result.getCnt());
			LOG.info("SUMMARY:");
			LOG.info("\t" + padRight("REQUEST", 50) 
			+ " | " + padRight("COUNT", 5)
			+ " | " + padRight("STATUS", 6)
			+ " | " + padRight("COMMENT", 50));
			
			// create summary for each ip
			List<AccessSummary> summaryList = dataService.viewAccessSummary(result.getIp(), param.getStartDate(), endDate);
			
			processSummaryList(summaryList, job);
			
			dataService.add(summaryList);
		}
		
	}

	/**
	 * Display summary list.
	 * 
	 * @param summaryList
	 * @param job
	 */
	private void processSummaryList(List<AccessSummary> summaryList, ParseJob job) {
		
		for(AccessSummary summary : summaryList) {
			summary.setParseJob(job); // set parent
			
			// check for readable reason
			String key = "reason.status." + summary.getStatus();
			String reason = env.getProperty(key, "");
			summary.setReason(reason);
			LOG.info("\t" + padRight(summary.getRequest(), 50) 
					+ " | " + padRight(String.valueOf(summary.getCnt()), 5)
					+ " | " + padRight(summary.getStatus(), 6)
					+ " | " + padRight(summary.getReason(), 50));
		}
		
	}
	
	/**
	 * Utility for padding space characters to the right.
	 * @param s
	 * @param n
	 * @return
	 */
	private String padRight(String s, int n) {
		 StringBuilder pattern = new StringBuilder();
		 pattern.append("%1$-").append(n).append("s");
		 
	     return String.format(pattern.toString(), s);  
	}


}
