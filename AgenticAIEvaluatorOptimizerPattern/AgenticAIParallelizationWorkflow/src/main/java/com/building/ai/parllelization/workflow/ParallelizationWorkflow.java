package com.building.ai.parllelization.workflow;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ParallelizationWorkflow {

    private final ChatClient chatClient;

    public ParallelizationWorkflow(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public List<String> parallel(String prompt, List<String> inputs, int nWorkers) {

        try (ExecutorService executor = Executors.newFixedThreadPool(nWorkers)) {
            // Create CompletableFuture for each input
            List<CompletableFuture<String>> futures = inputs.stream()
                    .map(input -> CompletableFuture.supplyAsync(() -> {
                        return chatClient.prompt(prompt + "\n\nContent: " + input)
                                .call()
                                .content();
                    }, executor))
                    .toList();

            // Collect results in order
            return futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        }
    }
}
