package com.detoxdigital.repository;

import com.detoxdigital.model.AlertEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AlertEventRepository extends JpaRepository<AlertEvent, Long> {

    long countByTriggeredAtBetween(LocalDateTime start, LocalDateTime end);
}
