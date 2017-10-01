package com.ef.orm.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

@Entity
public class ParseJob {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)  // use auto-increment
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date jobExecutionDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDateParam;
	
	private String durationParam;
	
	private int thresholdParam;
	
	@OneToMany(mappedBy="parseJob")
	private Collection<AccessSummary> summaries;

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
	 * @return the jobExecutionDate
	 */
	public Date getJobExecutionDate() {
		return jobExecutionDate;
	}

	/**
	 * @param jobExecutionDate the jobExecutionDate to set
	 */
	public void setJobExecutionDate(Date jobExecutionDate) {
		this.jobExecutionDate = jobExecutionDate;
	}

	/**
	 * @return the startDateParam
	 */
	public Date getStartDateParam() {
		return startDateParam;
	}

	/**
	 * @param startDateParam the startDateParam to set
	 */
	public void setStartDateParam(Date startDateParam) {
		this.startDateParam = startDateParam;
	}

	/**
	 * @return the durationParam
	 */
	public String getDurationParam() {
		return durationParam;
	}

	/**
	 * @param durationParam the durationParam to set
	 */
	public void setDurationParam(String durationParam) {
		this.durationParam = durationParam;
	}

	/**
	 * @return the thresholdParam
	 */
	public int getThresholdParam() {
		return thresholdParam;
	}

	/**
	 * @param thresholdParam the thresholdParam to set
	 */
	public void setThresholdParam(int thresholdParam) {
		this.thresholdParam = thresholdParam;
	}

	/**
	 * @return the summaries
	 */
	public Collection<AccessSummary> getSummaries() {
		return summaries;
	}

	/**
	 * @param summaries the summaries to set
	 */
	public void setSummaries(Collection<AccessSummary> summaries) {
		this.summaries = summaries;
	}
}
