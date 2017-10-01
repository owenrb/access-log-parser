package com.ef.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ef.orm.model.AccessSummary;

@Repository
public interface AccessSummaryRepository extends JpaRepository<AccessSummary, Long>{

}
