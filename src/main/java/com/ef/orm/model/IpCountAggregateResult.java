package com.ef.orm.model;

public class IpCountAggregateResult {

	private String ip;
	private int cnt;
	
	public IpCountAggregateResult(String ip, int cnt) {
		super();
		this.ip = ip;
		this.cnt = cnt;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the cnt
	 */
	public int getCnt() {
		return cnt;
	}
	
}
