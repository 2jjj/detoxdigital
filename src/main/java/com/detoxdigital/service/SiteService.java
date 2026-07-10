package com.detoxdigital.service;

import com.detoxdigital.model.DistractingSite;
import com.detoxdigital.repository.DistractingSiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    private final DistractingSiteRepository repository;

    public SiteService(DistractingSiteRepository repository) {
        this.repository = repository;
    }

    public List<DistractingSite> findAll() {
        return repository.findAll();
    }

    public List<DistractingSite> findActive() {
        return repository.findByActiveTrue();
    }

    public DistractingSite save(DistractingSite site) {
        return repository.save(site);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean isDistracting(String domain) {
        return repository.findByDomain(domain)
                .filter(DistractingSite::isActive)
                .isPresent();
    }

    public DistractingSite findByDomain(String domain) {
        return repository.findByDomain(domain).orElse(null);
    }
}
