package br.com.backend.controller;

import br.com.backend.dto.response.DashboardResponse;
import br.com.backend.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @Operation(summary = "Get the amount of entities registered")
    @GetMapping
    public DashboardResponse getDashboard() {
        return service.getDashboard();
    }
}
