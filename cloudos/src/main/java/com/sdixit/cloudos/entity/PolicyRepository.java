package com.sdixit.cloudos.entity;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;

public interface PolicyRepository extends TypedIdCassandraRepository<Policy, String> {

	@Query("select * from policy where id = ?0")
	Policy findById(String id);
}
