package com.detoxdigital.repository;

import com.detoxdigital.model.DistractingSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistractingSiteRepository extends JpaRepository<DistractingSite, Long> {

    Optional<DistractingSite> findByDomain(String domain);

    List<DistractingSite> findByActiveTrue();

    boolean existsByDomain(String domain);
}
