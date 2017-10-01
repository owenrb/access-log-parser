package com.ef.orm.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ef.orm.model.AccessSummary;
import com.ef.orm.model.IpCountAggregateResult;
import com.ef.orm.model.LogDetail;
import com.ef.orm.model.ParseJob;
import com.ef.orm.repository.AccessSummaryRepository;
import com.ef.orm.repository.LogDetailRepository;
import com.ef.orm.repository.ParseJobRepository;
import com.ef.vo.LogDetailVO;

@Component
public class DataAccessService {
	
	private static final Logger LOG = Logger.getLogger(DataAccessService.class);
	
	@Autowired
	@Qualifier("logDateFormatter")
	private SimpleDateFormat dateFormatter; 

    @Autowired
    private LogDetailRepository logdetailRepository;
    
    @Autowired
    private ParseJobRepository parsejobRepository;
    
    @Autowired
    private AccessSummaryRepository accessSummaryRepository;
    
    @Transactional
    public void add(LogDetail logdetail) {
    		logdetailRepository.save(logdetail);
    }
    
    @Transactional
    public void add(ParseJob parsejob) {
    		parsejobRepository.save(parsejob);
    }
    
    @Transactional
    public void add(AccessSummary accesssummary) {
    		accessSummaryRepository.save(accesssummary);
    }
    
    @Transactional
    public void add(List<AccessSummary> accesssummaries) {
    		accessSummaryRepository.save(accesssummaries);
    }
    
    public void add(LogDetailVO vo) {
    		LogDetail logdetail = new LogDetail();
    		
    		try {
			Date timestamp = dateFormatter.parse(vo.getTime());
			logdetail.setTime(timestamp);
			
			LOG.debug("time: " + timestamp);
		} catch (ParseException e) {
			LOG.error("Unable to parse time: " + vo.getTime(), e);
			return;
		}
    		logdetail.setIp(vo.getRemoteIpAddress());
    		logdetail.setRequest(vo.getUrl());
    		logdetail.setStatus(vo.getStatus());
    		
    		add(logdetail);
    }
	
	public List<IpCountAggregateResult> searchLogDetail(Date startDate, Date endDate, int threshold) {
		
		List<Object[]> resultSet = logdetailRepository.searchLogDetail(startDate, endDate, threshold);
		
		List<IpCountAggregateResult> list = new ArrayList<>();
		for(Object[] tuple : resultSet) {
			
			list.add(new IpCountAggregateResult((String) tuple[0], ((BigInteger) tuple[1]).intValue()));
		}
		
		return list;
		
	}
	
	public List<AccessSummary> viewAccessSummary(String ip, Date startDate, Date endDate) {
		List<Object[]> resultSet =  logdetailRepository.viewAccessSummary(ip, startDate, endDate);
		
		List<AccessSummary> list = new ArrayList<>();
		for(Object[] tuple : resultSet) {
			list.add(new AccessSummary((String) tuple[0], (String)tuple[1], 
					(String) tuple[2], ((BigInteger) tuple[3]).intValue())); 
		}
		
		return list;
		
	}

	/**
	 * @return the dateFormatter
	 */
	public SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}

	/**
	 * @param dateFormatter the dateFormatter to set
	 */
	public void setDateFormatter(SimpleDateFormat dateFormatter) {
		this.dateFormatter = dateFormatter;
	}

	/**
	 * @return the logdetailRepository
	 */
	public LogDetailRepository getLogdetailRepository() {
		return logdetailRepository;
	}

	/**
	 * @param logdetailRepository the logdetailRepository to set
	 */
	public void setLogdetailRepository(LogDetailRepository logdetailRepository) {
		this.logdetailRepository = logdetailRepository;
	}

	/**
	 * @return the parsejobRepository
	 */
	public ParseJobRepository getParsejobRepository() {
		return parsejobRepository;
	}

	/**
	 * @param parsejobRepository the parsejobRepository to set
	 */
	public void setParsejobRepository(ParseJobRepository parsejobRepository) {
		this.parsejobRepository = parsejobRepository;
	}

	/**
	 * @return the accessSummaryRepository
	 */
	public AccessSummaryRepository getAccessSummaryRepository() {
		return accessSummaryRepository;
	}

	/**
	 * @param accessSummaryRepository the accessSummaryRepository to set
	 */
	public void setAccessSummaryRepository(AccessSummaryRepository accessSummaryRepository) {
		this.accessSummaryRepository = accessSummaryRepository;
	}

}
