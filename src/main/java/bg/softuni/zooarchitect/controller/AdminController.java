package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final UserService userService;
    private final AnimalService animalService;
    private final CommentService commentService;
    private final HabitatService habitatService;
    private final ZooService zooService;

    public AdminController(UserService userService, AnimalService animalService, CommentService commentService, HabitatService habitatService, ZooService zooService) {
        this.userService = userService;
        this.animalService = animalService;
        this.commentService = commentService;
        this.habitatService = habitatService;
        this.zooService = zooService;
    }

    private boolean isAdmin () {
        return userService.getCurrentUser().getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getName()));
    }

    @GetMapping("/admin")
    public String viewAdmin() {
        if (isAdmin()) {
            return "admin";
        }
        return "redirect:/";
    }

    @DeleteMapping("/animals")
    public String deleteAnimals() {
        if (isAdmin()) {
            animalService.deleteAll();
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/comments")
    public String deleteComments() {
        if (isAdmin()) {
            commentService.deleteAll();
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/habitats")
    public String deleteHabitats() {
        if (isAdmin()) {
            habitatService.deleteAll();
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/zoos")
    public String deleteZoos() {
        if (isAdmin()) {
            zooService.deleteAll();
        }
        return "redirect:/admin";
    }
}
