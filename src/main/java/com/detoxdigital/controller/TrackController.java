package com.detoxdigital.controller;

import com.detoxdigital.model.AlertEvent;
import com.detoxdigital.model.SiteVisit;
import com.detoxdigital.service.SiteService;
import com.detoxdigital.service.TrackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TrackController {

    private final TrackService trackService;
    private final SiteService siteService;

    public TrackController(TrackService trackService, SiteService siteService) {
        this.trackService = trackService;
        this.siteService = siteService;
    }

    @PostMapping("/track")
    public ResponseEntity<?> trackVisit(@RequestBody SiteVisit visit) {
        SiteVisit saved = trackService.recordVisit(visit);
        return ResponseEntity.ok(Map.of("id", saved.getId(), "status", "recorded"));
    }

    @PostMapping("/alert")
    public ResponseEntity<?> trackAlert(@RequestBody AlertEvent alert) {
        AlertEvent saved = trackService.recordAlert(alert);
        return ResponseEntity.ok(Map.of("id", saved.getId(), "status", "recorded"));
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkSite(@RequestParam String domain) {
        boolean distracting = siteService.isDistracting(domain);
        return ResponseEntity.ok(Map.of(
                "domain", domain,
                "distracting", distracting
        ));
    }
}
