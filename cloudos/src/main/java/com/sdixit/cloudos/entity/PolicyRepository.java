package com.sdixit.cloudos.entity;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;

import java.util.List;

public interface PolicyRepository extends TypedIdCassandraRepository<Policy, String> {

	@Query("select * from policy where id = ?0")
	Policy findByPolicyId(String id);

	@Query("select * from policy where entity_name = ?0 ALLOW FILTERING")
	List<Policy> findByEntityName(String entityName);
}
