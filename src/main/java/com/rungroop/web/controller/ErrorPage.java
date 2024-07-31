package com.rungroop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ErrorPage {
    @GetMapping("error")
    public String getDefaultErrorPage(Model model) {
        model.addAttribute("errorMessage", "This page does not exist");
        return "ErrorPage";
    }

    @GetMapping("error_page")
    public String getErrorPage(Model model, @ModelAttribute("errorMessage") final String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        return "ErrorPage";
    }
}
