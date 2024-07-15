package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.AnimalCreationDTO;
import bg.softuni.zooarchitect.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/animals")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("")
    public String viewAnimals(Model model) {
        model.addAttribute("animalList", animalService.getAllAnimals());
        return "animals";
    }

    @GetMapping("/create")
    public String viewCreateAnimal(Model model)
    {
        model.addAttribute("animalDTO", new AnimalCreationDTO());
        return "animal-create";
    }

    @PostMapping("/create")
    public String createAnimal(
            @Valid @ModelAttribute("zooDTO") AnimalCreationDTO animalCreationDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "animal-create";
        }

        animalService.save(animalCreationDTO);

        return "redirect:/animals";
    }
}
