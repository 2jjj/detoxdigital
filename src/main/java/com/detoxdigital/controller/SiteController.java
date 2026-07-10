package com.detoxdigital.controller;

import com.detoxdigital.model.DistractingSite;
import com.detoxdigital.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    @GetMapping
    public List<DistractingSite> listAll() {
        return siteService.findAll();
    }

    @GetMapping("/active")
    public List<DistractingSite> listActive() {
        return siteService.findActive();
    }

    @PostMapping
    public ResponseEntity<DistractingSite> create(@RequestBody DistractingSite site) {
        DistractingSite saved = siteService.save(site);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        siteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
