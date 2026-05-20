package com.createtask.createtask.controller.uicontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Serves the Thymeleaf UI pages for the Project and Reports module.
// These routes are protected by Spring Security — only kavish role can access them.
@Controller
public class ProjectUiController {

    // Serves the project endpoints page — lists all 6 project APIs
    @GetMapping("/projects-ui")
    public String projectsUi() {
        return "project";
    }

    // Serves the reports endpoints page — lists the project summary dashboard API
    @GetMapping("/reports-ui")
    public String reportsUi() {
        return "project";
    }
}