package com.building.ai.service.dto;

import java.util.List;

public record OrchestratorAnalysis(
        String analysis,        // Understanding of the travel request
        String travelStrategy,  // Overall approach for this trip
        List<PlanningTask> tasks // Specific planning tasks to execute
) {
}
