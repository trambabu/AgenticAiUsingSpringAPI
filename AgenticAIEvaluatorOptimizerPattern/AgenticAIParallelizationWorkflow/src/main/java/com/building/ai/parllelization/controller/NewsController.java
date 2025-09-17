package com.building.ai.parllelization.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.building.ai.parllelization.service.NewsService;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/summarize")
    public ResponseEntity<Map<String, Object>> summarizeNews() {
        Map<String, Object> result = newsService.summarizeNews();
        return ResponseEntity.ok(result);
    }
}
