package bg.softuni.zooarchitect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/sign-up")
    public String viewSignUp() {
        return "sign-up";
    }
}
