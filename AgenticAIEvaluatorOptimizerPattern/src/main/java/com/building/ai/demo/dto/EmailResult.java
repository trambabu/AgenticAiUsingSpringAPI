package com.building.ai.demo.dto;


import java.util.List;

public record EmailResult(
        String email,
        List<String> improvementSteps
) {
}
