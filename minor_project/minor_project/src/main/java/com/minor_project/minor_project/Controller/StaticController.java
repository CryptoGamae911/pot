package com.minor_project.minor_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticController {
    @RequestMapping("/")
    public String home() {
        return "index.html"; // Default HTML file served
    }
}
