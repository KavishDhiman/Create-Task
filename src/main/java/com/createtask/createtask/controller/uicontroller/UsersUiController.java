package com.createtask.createtask.controller.uicontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Marks this class as Spring MVC controller
public class UsersUiController {

    @GetMapping("/users-ui") // Maps GET request for users UI page
    public String usersPage() {

        return "users"; // Returns users.html page
    }

}