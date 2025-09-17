package com.building.ai.service.dto;

public record TravelRequest(
        String destination,
        Integer numberOfDays,
        String budgetRange, // e.g., "budget", "mid-range", "luxury"
        String travelStyle, // e.g., "adventure", "relaxation", "cultural", "family"
        String groupSize,   // e.g., "solo", "couple", "family", "group"
        String specialInterests // e.g., "food", "history", "nature", "nightlife"
) {
}
