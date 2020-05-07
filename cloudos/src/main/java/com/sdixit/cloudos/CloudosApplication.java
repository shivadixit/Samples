package com.sdixit.cloudos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.sdixit.cloudos"})
public class CloudosApplication {

	public static void main(String[] args) {

		ApplicationContext ctx =  SpringApplication.run(CloudosApplication.class, args);

		PolicyExecutor policyExecutor = new PolicyExecutor();
        UserPolicyExecutor userPolicyExecutor = new UserPolicyExecutor();

		AutowireCapableBeanFactory awcbf = ctx.getAutowireCapableBeanFactory();
		awcbf.autowireBean(policyExecutor);
		awcbf.autowireBean(userPolicyExecutor);

		// Policy Executor
		policyExecutor.execute();

		//UserPolicy Executor
		userPolicyExecutor.execute();
	}

}
