package com.createtask.createtask.controller.uicontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// Serves the Thymeleaf UI page for the Notification module
// Follows Separation of Concerns by keeping UI navigation logic separate from REST APIs
@Controller
public class NotificationUiController {
    // Maps /notifications-ui route to the notification-api Thymeleaf template
    // Keeps controller lightweight by handling only page navigation responsibilities
    @GetMapping("/notifications-ui")
    public String notificationUi() {
        // Returns notification-api.html from the templates folder
        return "notification-api";
    }
}