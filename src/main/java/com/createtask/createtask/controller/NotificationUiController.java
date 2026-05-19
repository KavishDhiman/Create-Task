package com.createtask.createtask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationUiController {

    @GetMapping("/notifications-ui")
    public String notificationUi() {
        return "notification-api";
    }
}