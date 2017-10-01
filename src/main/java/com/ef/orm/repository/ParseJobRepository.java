package com.ef.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ef.orm.model.ParseJob;

@Repository
public interface ParseJobRepository extends JpaRepository<ParseJob, Long>{

}
