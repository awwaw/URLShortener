package com.rungroop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorPage {
    @GetMapping("{anything}")
    public String getDefaultErrorPage(Model model, @PathVariable("anything") final String path) {
        if (path.equals("error_page")) {
            return "redirect:/error_page";
        }
        model.addAttribute("errorMessage", "This page does not exist");
        return "ErrorPage";
    }

    @GetMapping("error_page")
    public String getErrorPage(Model model, @ModelAttribute("errorMessage") String errorMessage) {
        if (errorMessage.isEmpty()) {
            errorMessage = "This page does not exist";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "ErrorPage";
    }
}
