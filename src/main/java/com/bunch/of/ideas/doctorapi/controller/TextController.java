package com.bunch.of.ideas.doctorapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class TextController {


    @GetMapping("/chat")
    public String handleSubmit(Model model) {
//        List<Message> messageList = new ArrayList<>();
        model.addAttribute("messageList", new ArrayList<>());
        return "chat";
    }

    @GetMapping("/history")
    public String handleHistory(Model model) {
//        List<Message> messageList = this.messageService.getMessagesForUser("doctor@example.com");
//        model.addAttribute("messageList", messageList);
//        model.addAttribute("weeklyData", Arrays.asList(4, 4, 4, 5, 4, 3, 2));
//        model.addAttribute("monthlyData", Arrays.asList(4, 4, 4, 5, 4, 3, 2, 4, 4, 4, 5, 4, 3, 2, 4, 4, 4, 5, 4, 3, 2, 4, 4, 4, 5, 4, 3, 2, 5, 5));
        return "history";
    }
}
