package com.building.ai.service.dto;

import java.util.List;

public record TravelItinerary(
        String destination,
        String travelStrategy,
        String analysis,
        List<String> planningResults, // Results from each worker
        String finalItinerary,        // Combined day-by-day plan
        long processingTimeMs
) {
}