package com.sdixit.cloudos.entity;

import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;

public interface UserPolicyRepository extends TypedIdCassandraRepository<UserPolicy, String> {

	@Query("select * from cloudleaf.user_policy where user_id = ?0 and entity_name = ?1")
	List<UserPolicy> findByUserIdAndEntityName(String userId, String entityName);

	@Query("select * from cloudleaf.user_policy where user_id = ?0 and entity_name = ?1 and policy_id = ?2")
	UserPolicy findByUserIdEntityNameAndPolicyId(String userId, String entityName, String policy_id);
}
