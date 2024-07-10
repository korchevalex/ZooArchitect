package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.ZooCreationDTO;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.service.UserService;
import bg.softuni.zooarchitect.service.ZooService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/zoos")
public class ZooController {
    private final ZooService zooService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ZooController(ZooService zooService, UserService userService, ModelMapper modelMapper) {
        this.zooService = zooService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public String viewZoos(Model model) {

        model.addAttribute("zooList", zooService.getAllZoos());
        return "zoos";
    }

    private boolean zooExists() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        return user.getZoo() != null;
    }

    @GetMapping("/create")
    public String viewZooCreation(Model model) {
        if (zooExists()) {
            return "redirect:/zoos";
        }
        model.addAttribute("zooDTO", new ZooCreationDTO());
        return "zoo-create";
    }

    @PostMapping("/create")
    public String createZoo(
            @Valid @ModelAttribute("zooDTO") ZooCreationDTO zooCreationDTO,
            BindingResult bindingResult) {
        if (zooExists()) {
            return "redirect:/zoos";
        }
        if (bindingResult.hasErrors()) {
            return "zoo-create";
        }

        Zoo zoo = modelMapper.map(zooCreationDTO, Zoo.class);

        zooService.save(zoo);

        return "redirect:/zoos";
    }
}
