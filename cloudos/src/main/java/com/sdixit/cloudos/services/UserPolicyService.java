package com.sdixit.cloudos.services;

import java.util.List;

import com.sdixit.cloudos.entity.Policy;
import com.sdixit.cloudos.entity.UserPolicy;

public interface UserPolicyService {

	List<UserPolicy> getByUserIdAndEntityName(String userId, String entityName);

	void createUserPolicy(String userId, Policy policy);

	void updateUserPolicy(String userId, Policy policy);

	void deleteUserPolicy(String userId, Policy policy);
}
