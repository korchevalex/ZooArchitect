package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.UserRegisterDTO;
import bg.softuni.zooarchitect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String viewRegister(Model model) {
        model.addAttribute("userDTO", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDTO") UserRegisterDTO userRegisterDTO, BindingResult result, Model model) {
        boolean hasErrors = result.hasErrors();

        if (userService.usernameIsTaken(userRegisterDTO)) {
            model.addAttribute("usernameIsTaken", true);
            hasErrors = true;
        }

        if (userService.emailIsTaken(userRegisterDTO)) {
            model.addAttribute("emailIsTaken", true);
            hasErrors = true;
        }

        if (!userService.save(userRegisterDTO)) {
            model.addAttribute("passwordMismatch", true);
            hasErrors = true;
        }

        if (hasErrors) {
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String viewLogin(@RequestParam(name = "error", required = false) String error, Model model){
        if (error != null) {
            model.addAttribute("credentialMismatch", true);
        }
        return "login";
    }
}
