package com.rungroop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexPage {

    @GetMapping({"/", ""})
    public String index() {
        return "IndexPage";
    }
}
