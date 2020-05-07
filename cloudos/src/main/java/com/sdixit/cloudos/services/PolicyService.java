package com.sdixit.cloudos.services;

import java.util.List;

import com.sdixit.cloudos.entity.Policy;
import com.sdixit.cloudos.dto.PolicyDTO;

public interface PolicyService {
	PolicyDTO getPolicy(String policyId);

	void createPolicy(PolicyDTO policyDTO);

	void updatePolicy(PolicyDTO policyDTO);

	void deletePolicy(String policyId);

	List<PolicyDTO> getPoliciesByTenantIDAndEntityName(String tenantId, String entityName);

    List<PolicyDTO> getPoliciesByTenantId(String tenantId);

	PolicyDTO convertToPolicyDTO(Policy p);
}
