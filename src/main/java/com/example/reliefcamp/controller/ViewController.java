package com.example.reliefcamp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "dashboard";
    }

    @GetMapping({"/dashboard", "/dashboard.html"})
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping({"/camps", "/camps.html"})
    public String camps() {
        return "camps";
    }

    @GetMapping({"/supplies", "/supplies.html"})
    public String supplies() {
        return "supplies";
    }

    @GetMapping({"/families", "/families.html"})
    public String families() {
        return "families";
    }

    @GetMapping({"/inventory", "/inventory.html", "/inventory-view"})
    public String inventoryView() {
        return "inventory";
    }

    @GetMapping({"/distributions", "/distributions.html", "/distributions-view"})
    public String distributionsView() {
        return "distributions";
    }

    @GetMapping({"/reports", "/reports.html"})
    public String reports() {
        return "reports";
    }
}
