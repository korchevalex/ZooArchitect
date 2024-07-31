package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.controller.aop.WarnIfExecutionExceeds;
import bg.softuni.zooarchitect.event.UserRegisteredEvent;
import bg.softuni.zooarchitect.model.dto.UserRegisterDTO;
import bg.softuni.zooarchitect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public UserController(UserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/register")
    public String viewRegister(Model model) {
        model.addAttribute("userDTO", new UserRegisterDTO());
        return "user/register";
    }

    @WarnIfExecutionExceeds(threshold = 100)
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
            return "user/register";
        }

        eventPublisher.publishEvent(new UserRegisteredEvent(this, userRegisterDTO));

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String viewLogin(@RequestParam(name = "error", required = false) String error, Model model){
        if (error != null) {
            model.addAttribute("credentialMismatch", true);
        }
        return "user/login";
    }
}
