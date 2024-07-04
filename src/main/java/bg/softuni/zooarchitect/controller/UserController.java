package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.UserSignUpDTO;
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

    @GetMapping("/sign-up")
    public String viewSignUp(Model model) {
        model.addAttribute("userDTO", new UserSignUpDTO());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid @ModelAttribute("userDTO") UserSignUpDTO userSignUpDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "sign-up";
        }

        if (!userSignUpDTO.getPassword().equals(userSignUpDTO.getConfirmPassword())) {
            model.addAttribute("passwordMismatch", true);
            return "sign-up";
        }

        User user = new User();
        user.setUsername(userSignUpDTO.getUsername());
        user.setPassword(userSignUpDTO.getPassword());
        user.setEmail(userSignUpDTO.getEmail());

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
