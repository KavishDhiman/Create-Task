package com.createtask.createtask.controller.uicontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersUiController {

    @GetMapping("/users-ui")
    public String usersPage() {
        return "users";
    }

}