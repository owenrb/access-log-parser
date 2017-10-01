package com.ef.orm.model;

import javax.persistence.*;

@Entity
public class AccessSummary {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)  // use auto-increment
	private Long id;
	
	@Column(length=20)
	private String ip;
	
	private String request;

	@Column(length=5)
	private String status;

	private int cnt;
	
	private String reason;
	
	public AccessSummary() {
		
	}

	public AccessSummary(String ip, String request, String status, int cnt) {
		this.ip = ip;
		this.request = request;
		this.status = status;
		this.cnt = cnt;
	}
	
	@ManyToOne
	private ParseJob parseJob;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the count
	 */
	public int getCnt() {
		return cnt;
	}

	/**
	 * @param cnt the count to set
	 */
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	/**
	 * @return the parseJob
	 */
	public ParseJob getParseJob() {
		return parseJob;
	}

	/**
	 * @param parseJob the parseJob to set
	 */
	public void setParseJob(ParseJob parseJob) {
		this.parseJob = parseJob;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
}
