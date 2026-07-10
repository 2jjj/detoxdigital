package com.detoxdigital.repository;

import com.detoxdigital.model.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {

    List<SiteVisit> findByVisitedAtBetweenOrderByVisitedAtDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s.domain, COUNT(s), SUM(s.durationSeconds) FROM SiteVisit s " +
           "WHERE s.visitedAt BETWEEN :start AND :end " +
           "GROUP BY s.domain ORDER BY SUM(s.durationSeconds) DESC")
    List<Object[]> findAggregatedStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    long countByVisitedAtBetween(LocalDateTime start, LocalDateTime end);
}
