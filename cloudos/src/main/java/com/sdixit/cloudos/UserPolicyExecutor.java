package com.sdixit.cloudos;

import com.sdixit.cloudos.entity.UserPolicy;
import com.sdixit.cloudos.services.UserPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;


import java.util.List;
@Configurable
public class UserPolicyExecutor {

    @Autowired
    public UserPolicyService userPolicyService;

    public void execute(){
        List<UserPolicy> userPolicy = userPolicyService.getByUserIdAndEntityName("shiva", "SHIPMENT");
        userPolicy.forEach(System.out::println);
    }
}
