package com.rungroop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorPage {
    @GetMapping({"error_page", "{ignored}"})
    public String getErrorPage(Model model, @ModelAttribute("errorMessage") String errorMessage) {
        if (errorMessage.isEmpty()) {
            errorMessage = "This page does not exist";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "ErrorPage";
    }
}
