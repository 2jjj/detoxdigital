package com.detoxdigital.service;

import com.detoxdigital.model.AlertEvent;
import com.detoxdigital.model.SiteVisit;
import com.detoxdigital.repository.AlertEventRepository;
import com.detoxdigital.repository.SiteVisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrackService {

    private final SiteVisitRepository visitRepository;
    private final AlertEventRepository alertRepository;

    public TrackService(SiteVisitRepository visitRepository, AlertEventRepository alertRepository) {
        this.visitRepository = visitRepository;
        this.alertRepository = alertRepository;
    }

    public SiteVisit recordVisit(SiteVisit visit) {
        return visitRepository.save(visit);
    }

    public AlertEvent recordAlert(AlertEvent alert) {
        return alertRepository.save(alert);
    }

    public List<Map<String, Object>> getTodayStats() {
        return aggregateStats(startOfDay(), endOfDay());
    }

    public List<Map<String, Object>> getWeekStats() {
        LocalDateTime start = LocalDate.now().minusDays(6).atStartOfDay();
        return aggregateStats(start, endOfDay());
    }

    public long getTotalVisitsToday() {
        return visitRepository.countByVisitedAtBetween(startOfDay(), endOfDay());
    }

    public long getAlertsToday() {
        return alertRepository.countByTriggeredAtBetween(startOfDay(), endOfDay());
    }

    private List<Map<String, Object>> aggregateStats(LocalDateTime start, LocalDateTime end) {
        List<Object[]> raw = visitRepository.findAggregatedStats(start, end);
        return raw.stream().map(row -> Map.<String, Object>of(
                "domain", row[0],
                "visits", row[1],
                "totalSeconds", row[2]
        )).collect(Collectors.toList());
    }

    private LocalDateTime startOfDay() {
        return LocalDate.now().atStartOfDay();
    }

    private LocalDateTime endOfDay() {
        return LocalDate.now().plusDays(1).atStartOfDay();
    }
}
