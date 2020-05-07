package com.sdixit.cloudos.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.sdixit.cloudos.entity.PolicyCondition;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
@Builder
@ToString
public class PolicyDTO implements Serializable {
	private static final long serialVersionUID = 63060565796836572L;

	private String id;

	private String tenantId;

	private String name;

	private PolicyCondition conditions;

	private Set<String> actions;

	private String entityName;

	private String moduleName;

	@JsonIgnore
	private boolean isAllowed;

	private String effect;

	private Date createdAt;

	private String createdBy;

	private Date modifiedAt;

	private String modifiedBy;

}
