package com.createtask.createtask.controller.uicontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Serves the Thymeleaf UI page for the Task, Category and TaskCategory module
@Controller
public class TaskUiController {

    // Maps /tasks-ui route to the task-api Thymeleaf template
    @GetMapping("/tasks-ui")
    public String taskUi() {
        return "task-api";
    }
}