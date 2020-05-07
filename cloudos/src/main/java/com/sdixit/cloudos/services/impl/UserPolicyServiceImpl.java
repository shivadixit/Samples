package com.sdixit.cloudos.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdixit.cloudos.entity.Policy;
import com.sdixit.cloudos.entity.UserPolicy;
import com.sdixit.cloudos.entity.UserPolicyRepository;
import com.sdixit.cloudos.services.UserPolicyService;

@Component("UserPolicyService")
public class UserPolicyServiceImpl implements UserPolicyService {

	@Autowired
	UserPolicyRepository userPolicyRepository;

	static final Logger LOG = LoggerFactory.getLogger(UserPolicyServiceImpl.class);

	@Override
	public List<UserPolicy> getByUserIdAndEntityName(String userId, String entityName) {
		List<UserPolicy> userPolicies = userPolicyRepository.findByUserIdAndEntityName(userId, entityName);
		return userPolicies;
	}

	@Override
	public void createUserPolicy(String userId, Policy policy) {
		try {
			UserPolicy userPolicy = UserPolicy.builder()
					.userId(userId)
					.policyId(policy.getId())
					.entityName(policy.getEntityName())
					.conditionExpression(policy.getConditionExpression())
					.targetExpression(policy.generateTargetExpression())
					.moduleName(policy.getModuleName())
					.build();
			userPolicyRepository.save(userPolicy);
		} catch (Exception e) {

			LOG.error("Couldn't create User Policy " + policy.getName() + " for " + userId + ":" + e.getMessage());
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public void updateUserPolicy(String userId, Policy policy) {
		if (getUserPolicy(userId, policy) != null) {
			try {
				createUserPolicy(userId, policy);
			} catch (Exception e) {
				LOG.error("Couldn't update Policy " + policy.getName() + " for " + userId + ":" + e.getMessage());
				throw new IllegalArgumentException(e.getMessage());
			}

		} else
			throw new IllegalArgumentException("User Policy not found");

	}

	@Override
	public void deleteUserPolicy(String userId, Policy policy) {
		UserPolicy userPolicy = getUserPolicy(userId, policy);
		if (userPolicy != null) {
			try {
				userPolicyRepository.delete(userPolicy);
			} catch (Exception e) {
				LOG.error("Couldn't update Policy " + policy.getName() + " for " + userId + ":" + e.getMessage());
				throw new IllegalArgumentException(e.getMessage());
			}

		} else
			throw new IllegalArgumentException("User Policy not found");

	}

	private UserPolicy getUserPolicy(String userId, Policy policy) {
		return userPolicyRepository.findByUserIdEntityNameAndPolicyId(userId, policy.getEntityName(), policy.getId());
	}
}
