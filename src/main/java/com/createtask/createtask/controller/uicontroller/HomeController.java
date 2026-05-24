package com.createtask.createtask.controller.uicontroller;

import org.springframework.stereotype.Controller; // Marks this class as a Spring MVC Controller
import org.springframework.ui.Model; // Used to pass data from controller to Thymeleaf UI pages
import org.springframework.web.bind.annotation.GetMapping; // Maps HTTP GET requests to controller methods

import java.util.List; // Used for storing feature list data

// Serves the main UI pages of the Task Management Application
// Follows MVC Architecture by handling only UI navigation and view rendering
@Controller
public class HomeController {

    // Maps the root URL (/) to the home page
    // Loads dynamic project details into the Thymeleaf UI using the Model object
    @GetMapping("/")
    public String home(Model model) {

        // Adds project title data to the UI model
        model.addAttribute("projectTitle", "Task Management System");

        // Adds project description data to the UI model
        // Keeps frontend content centralized and manageable
        model.addAttribute("projectDesc",
                "The Task Management Application is a centralized platform designed to simplify project and task management through an organized and user-focused workflow. It allows users to create projects, manage tasks, track progress, monitor deadlines, and maintain project-related activities in one place. The application supports essential features such as task categorization, comments, attachments, notifications, and productivity tracking to improve overall workflow management. With dedicated dashboards and reporting capabilities, users can easily monitor task status, project progress, and pending activities for better planning and execution. The system is designed to provide a smooth, structured, and efficient experience for managing daily work, improving productivity, and maintaining clear project organization across different users and functionalities.");

        // Adds application feature list for dynamic rendering in Thymeleaf
        model.addAttribute("features", List.of(
                "User & Role Management",
                "Project Tracking",
                "Task Assignment & Filtering",
                "Comments & Attachments",
                "Notifications & Reports"
        ));

        // Returns index.html from the templates folder
        return "index";
    }

    // Maps /response-view route to the response-view Thymeleaf template
    // Used for displaying API responses in a dedicated UI page
    @GetMapping("/response-view")
    public String responsePage() {

        // Returns response-view.html template
        return "response-view";
    }

    // Maps /access-denied route to the custom access denied page
    // Improves user experience by showing a proper authorization error screen
    @GetMapping("/access-denied")
    public String accessDenied() {

        // Returns access-denied.html template
        return "access-denied";
    }
}