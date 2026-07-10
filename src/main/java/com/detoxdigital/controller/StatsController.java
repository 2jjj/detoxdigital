package com.detoxdigital.controller;

import com.detoxdigital.service.TrackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final TrackService trackService;

    public StatsController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> today() {
        List<Map<String, Object>> sites = trackService.getTodayStats();
        long totalVisits = trackService.getTotalVisitsToday();
        long alerts = trackService.getAlertsToday();
        return ResponseEntity.ok(Map.of(
                "sites", sites,
                "totalVisits", totalVisits,
                "alerts", alerts
        ));
    }

    @GetMapping("/week")
    public ResponseEntity<Map<String, Object>> week() {
        List<Map<String, Object>> sites = trackService.getWeekStats();
        return ResponseEntity.ok(Map.of("sites", sites));
    }
}
