package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.ZooCreationDTO;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.service.AnimalService;
import bg.softuni.zooarchitect.service.UserService;
import bg.softuni.zooarchitect.service.ZooService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/zoos")
public class ZooController {
    private final ZooService zooService;
    private final UserService userService;
    private final AnimalService animalService;

    public ZooController(ZooService zooService, UserService userService, AnimalService animalService) {
        this.zooService = zooService;
        this.userService = userService;
        this.animalService = animalService;
    }

    @GetMapping("")
    public String viewZoos(Model model) {
        model.addAttribute("zooList", zooService.getAllZoos());
        model.addAttribute("zooExists", zooService.zooExists());
        return "zoos";
    }

    @GetMapping("/create")
    public String viewZooCreation(Model model) {
        if (zooService.zooExists()) {
            return "redirect:/zoos";
        }
        model.addAttribute("zooDTO", new ZooCreationDTO());
        return "zoo-create";
    }

    @PostMapping("/create")
    public String createZoo(
            @Valid @ModelAttribute("zooDTO") ZooCreationDTO zooCreationDTO,
            BindingResult bindingResult) {
        if (zooService.zooExists()) {
            return "redirect:/zoos";
        }
        if (bindingResult.hasErrors()) {
            return "zoo-create";
        }

        zooService.save(zooCreationDTO);

        return "redirect:/zoos";
    }

    @GetMapping("/{id}")
    @Transactional
    public String viewZooDetails(@PathVariable long id, Model model){
        Zoo zoo = zooService.getZooById(id);
        model.addAttribute("zoo", zoo);
        model.addAttribute("zooOwner", zoo.getOwner());
        model.addAttribute("userOwnsZoo", zoo.getOwner().equals(userService.getCurrentUser()));
        return "zoo-details";
    }

    @DeleteMapping("/{id}/delete")
    @Transactional
    public String deleteZoo(@PathVariable long id) {
        if (!zooService.delete(id)) {
            return "redirect:/zoos/" + id;
        }
        return "redirect:/zoos";
    }

    @GetMapping("/{id}/animals/add")
    @Transactional
    public String viewAddAnimal(Model model, @PathVariable long id) {
        Zoo zoo = zooService.getZooById(id);
        if (!userService.getCurrentUser().equals(zoo.getOwner())) {
            return "redirect:/zoos/" + id + "/animals";
        }
        model.addAttribute("animalList", animalService.getAllAnimals());
        model.addAttribute("zoo", zoo);
        return "zoo-animals-add";
    }

    @PostMapping("/{zooId}/animals/add/{animalId}")
    public String addAnimal(@PathVariable long animalId, @PathVariable long zooId) {
        zooService.addAnimalToZoo(animalId, zooId);
        return "redirect:/zoos/" + zooId + "/animals";
    }

    @GetMapping("/{id}/animals")
    @Transactional
    public String viewZooAnimals(@PathVariable long id, Model model) {
        Zoo zoo = zooService.getZooById(id);
        model.addAttribute("zoo", zoo);
        model.addAttribute("zooOwner", zoo.getOwner());
        model.addAttribute("animalList", zoo.getAnimals());
        return "zoo-animals";
    }

    @GetMapping("/my")
    @Transactional
    public String redirectToUserZoo() {
        User user = userService.getCurrentUser();
        Zoo zoo = user.getZoo();
        return "redirect:/zoos/" + zoo.getId();
    }
}
