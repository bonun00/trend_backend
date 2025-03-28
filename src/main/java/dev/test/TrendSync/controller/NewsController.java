package dev.test.TrendSync.controller;

import com.fasterxml.jackson.databind.JsonNode;
import dev.test.TrendSync.service.NaverNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NaverNewsService naverNewsService;

    @GetMapping("/it")
    public ResponseEntity<JsonNode> getITNews() {
        try {
            return naverNewsService.fetchITNews()
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}