package com.building.ai.parllelization.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.building.ai.parllelization.workflow.ParallelizationWorkflow;

@Service
public class NewsService {
    private final ParallelizationWorkflow parallelizationWorkflow;

    public NewsService(ParallelizationWorkflow parallelizationWorkflow) {
        this.parallelizationWorkflow = parallelizationWorkflow;
    }

    public Map<String, Object> summarizeNews() {

        String summarizationPrompt = getSummarizationPrompt();

        // Sample news content for different categories
        Map<String, String> newsContent = getNewsContent();

        List<String> categories = List.copyOf(newsContent.keySet());
        List<String> articles = categories.stream()
                .map(newsContent::get)
                .toList();

        long startTime = System.currentTimeMillis();

        // Execute parallel summarization with 4 concurrent workers
        List<String> summaries = parallelizationWorkflow.parallel(summarizationPrompt, articles, 4);

        long processingTime = System.currentTimeMillis() - startTime;

        // Combine categories with their summaries
        Map<String, String> categorizedSummaries = new HashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            categorizedSummaries.put(categories.get(i), summaries.get(i));
        }

        return Map.of(
                "summaries", categorizedSummaries,
                "processingTimeInMillis", processingTime,
                "categoriesProcessed", categories.size()
        );
    }

    private String getSummarizationPrompt() {
        return """
                Summarize the following news content in 2-3 sentences. 
                Focus on the key facts and main points. 
                Make it clear and easy to understand.
                """;
    }

    private Map<String, String> getNewsContent() {
        return Map.of(
                // Dummy articles
                "Technology",
                """
                        Apple announced its latest iPhone 15 series with significant upgrades including 
                        a new titanium design, improved camera system with 5x optical zoom, and USB-C 
                        connectivity replacing the Lightning port. The new phones also feature the A17 
                        Pro chip built on 3nm technology, offering better performance and battery life. 
                        Pre-orders start next Friday with prices starting at $799 for the base model.
                        """,

                "Sports",
                """
                        The World Cup final between Argentina and France delivered one of the most 
                        thrilling matches in football history. Lionel Messi scored twice in regular 
                        time and once in the penalty shootout, leading Argentina to victory after 
                        a 3-3 draw. The match went to extra time with Kylian Mbappe scoring a 
                        hat-trick for France. This victory marks Messi's first World Cup win.
                        """,

                "Business",
                """
                        Tesla reported record quarterly earnings with revenue reaching $25.2 billion, 
                        up 37% from the previous year. The company delivered over 466,000 vehicles 
                        in the quarter, exceeding analyst expectations. CEO Elon Musk announced 
                        plans to reduce vehicle prices further while maintaining profitability 
                        through improved manufacturing efficiency and cost reductions.
                        """,

                "Health",
                """
                        Scientists at Stanford University have developed a new blood test that can 
                        detect Alzheimer's disease up to 20 years before symptoms appear. The test 
                        measures specific proteins in the blood that indicate early brain changes. 
                        Clinical trials involving 1,000 participants showed 95% accuracy in 
                        identifying patients who would later develop the disease.
                        """
        );
    }
}
