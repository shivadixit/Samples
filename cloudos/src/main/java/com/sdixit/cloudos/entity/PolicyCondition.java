package com.sdixit.cloudos.entity;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PolicyCondition {
    @Data
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PolicyOperations {
        String lhs;
        String operator;
        String rhs;
    }

    @Data
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Conditions {
        List<PolicyOperations> should;
    }

    List<Conditions> must;
}
