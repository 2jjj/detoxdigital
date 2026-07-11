package com.detoxdigital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("active", "dashboard");
        return "dashboard";
    }

    @GetMapping("/sites")
    public String sites(Model model) {
        model.addAttribute("active", "sites");
        return "sites";
    }
}
