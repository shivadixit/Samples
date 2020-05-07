package com.sdixit.cloudos.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdixit.cloudos.entity.Policy;
import com.sdixit.cloudos.entity.PolicyRepository;
import com.sdixit.cloudos.dto.PolicyDTO;
import com.sdixit.cloudos.services.PolicyService;
import com.sdixit.cloudos.utils.SecurityUtils;

@Component("PolicyService")
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	PolicyRepository policyRepository;

	static final Logger LOG = LoggerFactory.getLogger(PolicyServiceImpl.class);

	@Override
	public PolicyDTO getPolicy(String policyId) {
		Policy policy = policyRepository.findById(policyId);

		return convertToPolicyDTO(policy);
	}

	@Override
	public void createPolicy(PolicyDTO policyDTO) {
		Policy policy = new Policy(policyDTO);
		if (StringUtils.isEmpty(policy.getId())) {
			policy.setId(UUID.randomUUID().toString());
		}
		if (policy.getCreatedAt() == null) {
			policy.setCreatedAt(Calendar.getInstance().getTime());
		}
		if (StringUtils.isEmpty(policy.getCreatedBy())) {
			policy.setCreatedBy(SecurityUtils.getCurrentUser());
		}
		try {
			if (validate(policy)) {
				policyRepository.save(policy);
			} else {
				throw new IllegalArgumentException("Data Validation Failed");
			}
		} catch (Exception e) {

			LOG.error("Couldn't create Policy " + policy.getName() + " for " + policy.getTenantId() + ":"
					+ e.getMessage());
			throw new IllegalArgumentException(e.getMessage());
		}

	}

	@Override
	public void updatePolicy(PolicyDTO policyDTO) {
		if (policyRepository.findById(policyDTO.getId()) != null) {
			try {
				if (policyDTO.getModifiedAt() == null) {
					policyDTO.setModifiedAt(Calendar.getInstance().getTime());
				}
				if (StringUtils.isEmpty(policyDTO.getModifiedBy())) {
					policyDTO.setModifiedBy(SecurityUtils.getCurrentUser());
				}
				createPolicy(policyDTO);
			} catch (Exception e) {
				LOG.error("Couldn't update Policy " + policyDTO.getName() + " for " + policyDTO.getTenantId() + ":"
						+ e.getMessage());
				throw new IllegalArgumentException(e.getMessage());
			}

		} else
			throw new IllegalArgumentException("Policy not found");

	}

	@Override
	public void deletePolicy(String policyId) {
		Policy policy = policyRepository.findById(policyId);
		if (policy != null) {
			try {
				policyRepository.delete(policy);
			} catch (Exception e) {
				LOG.error("Couldn't delete Policy " + policy.getName() + " for " + policy.getTenantId() + ":"
						+ e.getMessage());
				throw new IllegalArgumentException(e.getMessage());
			}

		} else
			throw new IllegalArgumentException("Policy not found");

	}

	@Override
	public List<PolicyDTO> getPoliciesByTenantId(String tenantId) {

		List<String> tenantIds;
		if (tenantId.equalsIgnoreCase(SecurityUtils.CLOUDLEAF_TENANCY))
			tenantIds = Arrays.asList(SecurityUtils.CLOUDLEAF_TENANCY);
		else
			tenantIds = Arrays.asList(SecurityUtils.CLOUDLEAF_TENANCY, tenantId);

		List<Policy> allTenantPolicies = new ArrayList<>(); // TODO Need to add ES implementation to fetch all Policies
		// applicable for the tenant

		return allTenantPolicies.stream().map(p -> convertToPolicyDTO(p)).collect(Collectors.toList());
	}

	@Override
	public List<PolicyDTO> getPoliciesByTenantIDAndEntityName(String tenantId, String entityName) {
		List<PolicyDTO> policyDTOs = new ArrayList<>();
		// TODO Elastic Search Implementation
		return policyDTOs;
	}

	private boolean validate(Policy policy) {
		// TODO

		return true;

	}

	public PolicyDTO convertToPolicyDTO(Policy p) {
		PolicyDTO policy = PolicyDTO.builder().id(p.getId()).tenantId(p.getTenantId()).name(p.getName())
				.entityName(p.getEntityName()).actions(p.getActions()).isAllowed(p.isAllowed())
				.moduleName(p.getModuleName()).build();

		policy.setConditions(p.getPolicyConditions());

		if (p.isAllowed()) {
			policy.setEffect(Policy.ALLOW_ACCESS);
		} else {
			policy.setEffect(Policy.DENY_ACCESS);
		}

		return policy;
	}
}
