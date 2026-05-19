package com.createtask.createtask.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("projectTitle", "Task Management System");

        model.addAttribute("projectDesc",
                "A full-stack REST API built with Spring Boot and JPA. " +
                        "Manages users, roles, projects, tasks, comments, attachments, " +
                        "and notifications — with reporting endpoints.");

        model.addAttribute("features", List.of(
                "User & Role Management",
                "Project Tracking",
                "Task Assignment & Filtering",
                "Comments & Attachments",
                "Notifications & Reports"
        ));

        return "index";
    }
}
