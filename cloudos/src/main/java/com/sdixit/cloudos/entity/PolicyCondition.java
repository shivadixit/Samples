package com.sdixit.cloudos.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class PolicyCondition {
    @Data
    @Builder
    @ToString
    public static class PolicyOperations {
        String lhs;
        String operator;
        String rhs;
    }

    @Data
    @Builder
    @ToString
    public static class Conditions {
        List<PolicyOperations> should;
    }

    List<Conditions> must;
}
