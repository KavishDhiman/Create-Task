package com.createtask.createtask.controller.uicontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttachmentCommentUiController {

    @GetMapping("/attachment-comment-ui")
    public String attachmentCommentPage() {
        return "attachment-comment";
    }

}