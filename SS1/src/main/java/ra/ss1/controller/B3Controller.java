package ra.ss1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class B3Controller {
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello, Spring Boot API!");
        return "hello";
    }
}
