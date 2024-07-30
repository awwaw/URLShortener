package com.rungroop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorPage {

    @GetMapping("/error_page")
    public String getErrorPage() {
        return "ErrorPage";
    }
}
