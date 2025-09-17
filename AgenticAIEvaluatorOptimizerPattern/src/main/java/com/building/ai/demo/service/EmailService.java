package com.building.ai.demo.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.building.ai.demo.dto.EmailRequest;
import com.building.ai.demo.dto.EmailResponse;
import com.building.ai.demo.dto.EmailResult;
import com.building.ai.demo.workflow.EmailEvaluatorOptimizer;

@Service
public class EmailService {

    private final EmailEvaluatorOptimizer optimizer;

    public EmailService(EmailEvaluatorOptimizer optimizer) {
        this.optimizer = optimizer;
    }

    public EmailResponse generateEmail(EmailRequest request) {
        try {
            // Use our Evaluator-Optimizer to create the perfect email
            EmailResult result = optimizer.createPerfectEmail(
                    request.emailType(),
                    request.recipientName(),
                    request.mainMessage(),
                    request.tonePreference()
            );

            // Convert to response format
            return new EmailResponse(
                    result.email(),
                    (result.improvementSteps().size() / 2) - 1,
                    result.improvementSteps()
            );

        } catch (Exception e) {
            // If something goes wrong, tell the user nicely
            return new EmailResponse(
                    "Sorry, we couldn't generate your email. Please try again.",
                    0,
                    List.of("Error occurred: " + e.getMessage())
            );
        }
    }
}
