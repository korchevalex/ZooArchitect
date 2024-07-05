package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.UserRegisterDTO;
import bg.softuni.zooarchitect.model.entity.User;
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
    public String viewSignUp(Model model) {
        model.addAttribute("userDTO", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/sign-up")
    public String register(@Valid @ModelAttribute("userDTO") UserRegisterDTO userRegisterDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            model.addAttribute("passwordMismatch", true);
            return "register";
        }

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());

        userService.save(user);
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
