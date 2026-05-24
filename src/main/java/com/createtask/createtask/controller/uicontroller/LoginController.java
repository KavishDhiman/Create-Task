package com.createtask.createtask.controller.uicontroller;

import org.springframework.stereotype.Controller; // Marks this class as a Spring MVC Controller
import org.springframework.web.bind.annotation.GetMapping; // Maps HTTP GET requests to controller methods

// Serves the custom login UI page for the application
// Follows Separation of Concerns by keeping authentication UI routing separate from business logic
@Controller
public class LoginController {

    // Maps /login route to the login Thymeleaf template
    // Used by Spring Security to display the custom login page
    @GetMapping("/login")
    public String loginPage() {

        // Returns login.html from the templates folder
        return "login";
    }
}