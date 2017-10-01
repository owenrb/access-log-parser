package com.ef.orm.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ef.orm.model.LogDetail;

@Repository
public interface LogDetailRepository extends JpaRepository<LogDetail, Long> {

	/**
	 * Find IP with more than the number of requests specified in the given period and threshold.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param threshold
	 * @return
	 */
	@Query(nativeQuery=true,
		value = "SELECT ip, COUNT(*) as cnt "
			+ "FROM LogDetail "
			+ "WHERE time between :startDate and :endDate "
			+ "GROUP BY ip "
			+ "HAVING COUNT(*) > :threshold")
	List<Object[]> searchLogDetail(
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate,
			@Param("threshold") int threshold);

	@Query(nativeQuery=true,
		value = "SELECT ip, request, status, COUNT(*) as cnt "
			+ "FROM LogDetail "
			+ "WHERE ip = :ip and (time between :startDate and :endDate) "
			+ "GROUP BY ip, request, status ")
	List<Object[]> viewAccessSummary(
			@Param("ip") String ip, 
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
}
