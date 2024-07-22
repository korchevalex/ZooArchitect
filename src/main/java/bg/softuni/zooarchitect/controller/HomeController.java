package bg.softuni.zooarchitect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String viewIndex() {
        return "index";
    }

    @GetMapping("/elements")
    public String viewElements() {
        return "elements";
    }

    @GetMapping("/about")
    public String viewAbout() {
        return "about";
    }
}
