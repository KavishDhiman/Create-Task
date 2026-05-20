package com.createtask.createtask.controller.uicontroller;
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
                "The Task Management Application is a centralized platform designed to simplify project and task management through an organized and user-focused workflow. It allows users to create projects, manage tasks, track progress, monitor deadlines, and maintain project-related activities in one place. The application supports essential features such as task categorization, comments, attachments, notifications, and productivity tracking to improve overall workflow management. With dedicated dashboards and reporting capabilities, users can easily monitor task status, project progress, and pending activities for better planning and execution. The system is designed to provide a smooth, structured, and efficient experience for managing daily work, improving productivity, and maintaining clear project organization across different users and functionalities.");

        model.addAttribute("features", List.of(
                "User & Role Management",
                "Project Tracking",
                "Task Assignment & Filtering",
                "Comments & Attachments",
                "Notifications & Reports"
        ));

        return "index";
    }
    @GetMapping("/response-view")
    public String responsePage() {
        return "response-view";
    }
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
