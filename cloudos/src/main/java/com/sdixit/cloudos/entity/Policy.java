package com.sdixit.cloudos.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sdixit.cloudos.dto.PolicyDTO;
import com.datastax.driver.core.DataType.Name;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity Class for "policy" table
 * @author shiva.dixit
 */

@Data
@Table("policy")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Policy {
	@PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@CassandraType(type = Name.TEXT)
	protected String id;

	@PrimaryKeyColumn(name = "tenant_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	protected String tenantId;

	@Column("name")
	private String name;

	@Column("conditions")
	@CassandraType(type = Name.MAP, typeArguments = { Name.TEXT, Name.TEXT })
	private Map<String, String> conditions;

	@CassandraType(type = Name.SET, typeArguments = { Name.TEXT })
	protected Set<String> actions;

	@Column("entity_name")
	private String entityName;

	@Column("is_allowed")
	private boolean isAllowed;

	@Column("condition_spring_el")
	private String conditionExpression;

	@Column("target_spring_el")
	private String targetExpression;

	@Column("module_name")
	private String moduleName;

	@Column("created_at")
	private Date createdAt;

	@Column("created_by")
	private String createdBy;

	@Column("modified_at")
	private Date modifiedAt;

	@Column("modified_by")
	private String modifiedBy;

	public static final String ALLOW_ACCESS = "ALLOW";
	public static final String DENY_ACCESS = "DENY";
	public static final String KEY_MUST = "must";
	public static final String KEY_SHOULD = "should";


	public Policy(PolicyDTO policyDTO) {
		this.id = policyDTO.getId();
		this.tenantId = policyDTO.getTenantId();
		this.name = policyDTO.getName();
		this.entityName = policyDTO.getEntityName();
		this.actions = policyDTO.getActions();
		this.moduleName = policyDTO.getModuleName();
		this.createdAt = policyDTO.getCreatedAt();
		this.createdBy = policyDTO.getCreatedBy();
		this.modifiedAt = policyDTO.getModifiedAt();
		this.modifiedBy = policyDTO.getModifiedBy();

		if (policyDTO.getEffect().equalsIgnoreCase(ALLOW_ACCESS)) {
			this.isAllowed = true;
		} else {
			this.isAllowed = false;
		}

		setConditions(policyDTO.getConditions());

		this.conditionExpression = generateConditionExpression();
		this.targetExpression = generateTargetExpression();
	}

	public void setConditions(PolicyCondition pc) {
		JSONObject jsonObject = new ObjectMapper().convertValue(pc, new TypeReference<JSONObject>(){});
		Map<String, Object> objectMap = JsonFlattener.flattenAsMap(jsonObject.toString());
		this.conditions = new HashMap<>();
		objectMap.keySet().forEach(k -> this.conditions.put(k, String.valueOf(objectMap.get(k))));
	}

	public PolicyCondition getPolicyConditions(){
		PolicyCondition pc = null;
		if(this.conditions == null){
			return pc;
		}

		JSONParser parser = new JSONParser();
		try {
			String jsonString = this.conditions.keySet().stream()
					.map(key -> {
						StringBuffer condition = new StringBuffer("\"" + key + "\":");
						if (StringUtils.isNotBlank((String) this.conditions.get(key))) {
							condition.append("\"" + this.conditions.get(key) + "\"");
						} else {
							condition.append("{}");
						}
						return condition.toString();
					}).collect(Collectors.joining(", ", "{", "}"));

			String nestedJson = JsonUnflattener.unflatten(jsonString);

			JSONObject conditionObject = (JSONObject) parser.parse(nestedJson);

			pc = new ObjectMapper().convertValue(conditionObject,
					new TypeReference<PolicyCondition>() {
					});
		} catch (Exception e) {
				e.printStackTrace();
		}

		return  pc;
	}

	public String generateConditionExpression() {
		StringBuilder conditionExpr = null;
	/*	StringBuilder mustConditionExpr = new StringBuilder();
		StringBuilder shouldConditionExpr = new StringBuilder();

		this.conditions.keySet().forEach(k -> {
			if(k.equalsIgnoreCase(KEY_MUST)){
				this.conditions.get(k).getConditions().forEach(c -> {
					if(StringUtils.isEmpty(mustConditionExpr)){
						mustConditionExpr.append("("+ c.lhs+" " +c.operator +" "+ c.rhs +")");
					} else {
						mustConditionExpr.append("&& ("+ c.lhs+" " +c.operator +" "+ c.rhs +")");
					}
				});
			} else if(k.equalsIgnoreCase(KEY_SHOULD)){
				this.conditions.get(k).getConditions().forEach(c -> {
					if(StringUtils.isEmpty(shouldConditionExpr)){
						shouldConditionExpr.append("("+ c.lhs+" " +c.operator +" "+ c.rhs +")");
					} else {
						shouldConditionExpr.append("|| ("+ c.lhs+" " +c.operator +" "+ c.rhs +")");
					}
				});
			}
		});
		conditionExpr = new StringBuilder("(" + mustConditionExpr + ") && (" + shouldConditionExpr + ")"); */
		return null; // conditionExpr.toString();
	}

	public String generateTargetExpression() {
		StringBuilder targetExpression = new StringBuilder("{");
		this.actions.stream().map(exp -> entityName+"_"+exp).
				forEach(action -> targetExpression.append("\""+action+"\","));
		targetExpression.replace(0, targetExpression.length(), targetExpression.substring(0, targetExpression.length()-1));
		targetExpression.append("}.contains(action)");
		return targetExpression.toString();
	}
}
