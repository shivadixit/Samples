package com.sdixit.cloudos;

import com.sdixit.cloudos.dto.PolicyDTO;
import com.sdixit.cloudos.entity.Policy;
import com.sdixit.cloudos.entity.PolicyCondition;
import com.sdixit.cloudos.entity.PolicyRepository;
import com.sdixit.cloudos.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.*;

@Configurable
public class PolicyExecutor {

    @Autowired
    public PolicyService policyService;

    @Autowired
    PolicyRepository policyRepository;

    public void execute(){
        PolicyDTO policy = policyService.getPolicy("6a1df884-8e30-11ea-bc55-0242ac130003");
        System.out.println(policy.toString());

        List<PolicyCondition.PolicyOperations> orOps1 = Arrays.asList(
                PolicyCondition.PolicyOperations.builder().lhs("Shipment.origin").operator("==").rhs("BNG").build(),
                PolicyCondition.PolicyOperations.builder().lhs("Shipment.origin").operator("==").rhs("HYD").build()
        );

        List<PolicyCondition.PolicyOperations> orOps2 = Arrays.asList(
                PolicyCondition.PolicyOperations.builder().lhs("Shipment.destination").operator("==").rhs("BNG").build(),
                PolicyCondition.PolicyOperations.builder().lhs("Shipment.destination").operator("==").rhs("HYD").build()
        );

        PolicyCondition pc = PolicyCondition.builder()
                .must(Arrays.asList(
                        PolicyCondition.Conditions.builder().should(orOps1).build(),
                        PolicyCondition.Conditions.builder().should(orOps2).build()
                ))
                .build();

        PolicyDTO p = PolicyDTO.builder()
                .id("b57f71cb-2022-4ef8-8cec-b8797e932baf")
                .entityName("SHIPMENT")
                .name("SHIPMENT_RECEIVER_RESTRICTION1")
                .actions(new HashSet<String>(Arrays.asList("CREATE", "UPDATE")))
                .tenantId("ddadc454-0dc1-4fd2-aecc-1684918962e9")
                .isAllowed(true)
                .effect(Policy.ALLOW_ACCESS)
                .conditions(pc)
                .moduleName("INTRANSIT")
                .createdAt(Calendar.getInstance().getTime())
                .createdBy("shiva")
                .build();

        policyService.createPolicy(p);

        policyRepository.findByEntityName("SHIPMENT").forEach(pol -> {
            System.out.println("POLICY: "+pol);
            System.out.println("DTO : "+policyService.convertToPolicyDTO(pol));
        });
    }
}
