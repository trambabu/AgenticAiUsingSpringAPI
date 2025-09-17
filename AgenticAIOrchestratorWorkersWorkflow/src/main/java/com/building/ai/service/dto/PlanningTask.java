package com.building.ai.service.dto;

public record PlanningTask(
        String taskType,        // e.g., "accommodation", "activities", "dining"
        String description,     // What this task should accomplish
        String specialization   // Specific focus area for this task
) {
}
