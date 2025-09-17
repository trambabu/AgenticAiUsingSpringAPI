package com.building.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.building.ai.service.TravelPlanningService;
import com.building.ai.service.dto.TravelItinerary;
import com.building.ai.service.dto.TravelRequest;

@RestController
@RequestMapping("/api/travel")
public class TravelController {

    private final TravelPlanningService travelService;

    public TravelController(TravelPlanningService travelService) {
        this.travelService = travelService;
    }

    @PostMapping("/plan")
    public ResponseEntity<TravelItinerary> createItinerary(@RequestBody TravelRequest request) {
        try {
            TravelItinerary itinerary = travelService.planTrip(request);
            return ResponseEntity.ok(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error creating travel itinerary: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}