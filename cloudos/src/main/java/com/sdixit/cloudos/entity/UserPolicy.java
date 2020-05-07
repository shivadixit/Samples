package com.sdixit.cloudos.entity;

import lombok.*;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Entity Class for "user_policy" table, This table will be used by IAM Enforcement Module,
 * User and Policy accosiation need to be updated here for following use cases:
 * 1. When a Policy is associated with a Role, all User having that Role need to be inserted for policy association
 * 2. When a Policy is updated, All rows containing that Policy need to be updated for columns "entity_name,
 * condition_spring_el, target_spring_el"
 * 3. When a Policy is deleted, All rows containing that Policy need to be deleted
 * 4. When a Role to Policy association is deleted, All rows containing that Policy need to be deleted
 * for the Users who are having those roles
 *
 * @author shiva.dixit
 */

@Data
@Table("user_policy")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class UserPolicy {
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	protected String userId;

	@PrimaryKeyColumn(name = "policy_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
	protected String policyId;

	@PrimaryKeyColumn(name = "entity_name", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String entityName;

	@Column("condition_spring_el")
	private String conditionExpression;

	@Column("target_spring_el")
	private String targetExpression;

	@Column("module_name")
	private String moduleName;
}
