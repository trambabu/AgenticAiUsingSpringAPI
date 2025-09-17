package com.building.ai.service;

import org.springframework.stereotype.Service;

import com.building.ai.service.dto.TravelItinerary;
import com.building.ai.service.dto.TravelRequest;
import com.building.ai.service.workflow.TravelOrchestratorWorkflow;

@Service
public class TravelPlanningService {

    private final TravelOrchestratorWorkflow orchestratorWorkflow;

    public TravelPlanningService(TravelOrchestratorWorkflow orchestratorWorkflow) {
        this.orchestratorWorkflow = orchestratorWorkflow;
    }

    public TravelItinerary planTrip(TravelRequest request) {
        // Delegate to the workflow for AI processing
        return orchestratorWorkflow.createTravelPlan(request);
    }
}
