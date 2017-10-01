package com.ef.vo;

import java.util.List;

public class RegexVO {

	private String logformat;
	private String regex;
	private List<DirectiveVO> directives;
	
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
	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}
	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	/**
	 * @return the directives
	 */
	public List<DirectiveVO> getDirectives() {
		return directives;
	}
	/**
	 * @param directives the directives to set
	 */
	public void setDirectives(List<DirectiveVO> directives) {
		this.directives = directives;
	}
	
	
}
