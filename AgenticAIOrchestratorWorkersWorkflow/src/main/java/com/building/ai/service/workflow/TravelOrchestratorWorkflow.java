package com.building.ai.service.workflow;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import com.building.ai.service.dto.OrchestratorAnalysis;
import com.building.ai.service.dto.PlanningTask;
import com.building.ai.service.dto.TravelItinerary;
import com.building.ai.service.dto.TravelRequest;

@Component
public class TravelOrchestratorWorkflow {

    private final ChatClient chatClient;

    public TravelOrchestratorWorkflow(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * The orchestrator coordinates the end-to-end travel planning workflow.
     */
    public TravelItinerary createTravelPlan(TravelRequest request) {
        long startTime = System.currentTimeMillis();

        // Step 1: Orchestrator analyzes the travel request
        System.out.println("🎯 Orchestrator analyzing travel request for " + request.destination() + "...");

        String orchestratorPrompt = String.format(
                ORCHESTRATOR_PROMPT_TEMPLATE,
                request.destination(),
                request.numberOfDays(),
                request.budgetRange(),
                request.travelStyle() != null ? request.travelStyle() : "general exploration",
                request.groupSize() != null ? request.groupSize() : "general",
                request.specialInterests() != null ? request.specialInterests() : "general sightseeing"
        );

        OrchestratorAnalysis analysis = chatClient.prompt()
                .user(orchestratorPrompt)
                .call()
                .entity(OrchestratorAnalysis.class);

        System.out.println("📋 Travel Strategy: " + analysis.travelStrategy());
        System.out.println("📝 Planning tasks identified: " + analysis.tasks().size());

        // Step 2: Workers handle different aspects of trip planning in parallel
        System.out.println("⚡ Workers creating specialized recommendations...");

        List<CompletableFuture<String>> workerFutures = analysis.tasks().stream()
                .map(task -> CompletableFuture.supplyAsync(() ->
                        executePlanningTask(request, task)))
                .toList();

        // Wait for all workers to complete and collect results
        List<String> planningResults = workerFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // Step 3: Synthesize all recommendations into a final itinerary
        System.out.println("🔧 Synthesizing final travel itinerary...");

        String finalItinerary = synthesizeItinerary(request, analysis, planningResults);

        long processingTime = System.currentTimeMillis() - startTime;
        System.out.println("✅ Travel itinerary completed in " + processingTime + "ms");

        return new TravelItinerary(
                request.destination(),
                analysis.travelStrategy(),
                analysis.analysis(),
                planningResults,
                finalItinerary,
                processingTime
        );
    }

    /**
     * Executes an individual planning task (accommodation, activity, etc.)
     */
    private String executePlanningTask(TravelRequest request, PlanningTask task) {
        System.out.println("🔧 Worker handling: " + task.taskType());

        String workerPrompt = String.format(
                WORKER_PROMPT_TEMPLATE,
                request.destination(),
                request.numberOfDays(),
                task.taskType(),
                task.description(),
                task.specialization(),
                request.budgetRange(),
                request.travelStyle() != null ? request.travelStyle() : "general exploration",
                request.groupSize() != null ? request.groupSize() : "general",
                request.specialInterests() != null ? request.specialInterests() : "general sightseeing"
        );

        return chatClient.prompt()
                .user(workerPrompt)
                .call()
                .content();
    }

    /**
     * Combines all planning task results into a final itinerary
     */
    private String synthesizeItinerary(TravelRequest request, OrchestratorAnalysis analysis,
                                       List<String> planningResults) {
        String combinedResults = String.join("\n\n", planningResults);

        String synthesisPrompt = String.format(
                SYNTHESIZER_PROMPT_TEMPLATE,
                request.destination(),
                request.numberOfDays(),
                analysis.travelStrategy(),
                combinedResults,
                request.numberOfDays()
        );

        return chatClient.prompt()
                .user(synthesisPrompt)
                .call()
                .content();
    }

    // Prompt templates
    private static final String ORCHESTRATOR_PROMPT_TEMPLATE = """
            Think of you as a travel planner. Analyze this travel request and determine what aspects of the trip need to be planned:
            
            Destination: %s
            Duration: %s days
            Budget: %s
            Travel Style: %s
            Group: %s
            Special Interests: %s
            
            Based on this information, create a travel strategy and break it down into 3-4 specific planning tasks.
            Each task should handle different aspects of travel (accommodation, activities, dining, transportation).
            
            Respond in JSON format:
            {
              "analysis": "Your analysis of the destination and traveler preferences",
              "travelStrategy": "Overall strategy for this trip type and destination", 
              "tasks": [
                {
                  "taskType": "accommodation",
                  "description": "Find suitable places to stay based on budget and preferences",
                  "specialization": "Focus on location, amenities, and value for the specified budget"
                },
                {
                  "taskType": "activities", 
                  "description": "Recommend activities and attractions matching travel style",
                  "specialization": "Focus on experiences that match the travel style and interests"
                }
              ]
            }
            """;

    private static final String WORKER_PROMPT_TEMPLATE = """
            Create travel recommendations based on these requirements:
            
            Destination: %s
            Trip Duration: %s days
            Planning Focus: %s
            Task Description: %s
            Specialization: %s
            Budget Range: %s
            Travel Style: %s
            Group Type: %s
            Special Interests: %s
            
            Provide detailed, practical recommendations that travelers can actually use.
            Include specific names, locations, and helpful tips where possible.
            """;

    private static final String SYNTHESIZER_PROMPT_TEMPLATE = """
            Create a comprehensive day-by-day travel itinerary using these planning results:
            
            Destination: %s
            Duration: %s days
            Travel Strategy: %s
            
            Planning Results:
            %s
            
            Combine all the recommendations into a cohesive %s-day itinerary.
            Organize by day and include practical details like timing, locations, and transitions between activities.
            Make it easy to follow and realistic for travelers.
            """;
}