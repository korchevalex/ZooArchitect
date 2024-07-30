package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.AnimalCreationDTO;
import bg.softuni.zooarchitect.model.entity.Animal;
import bg.softuni.zooarchitect.service.AnimalService;
import bg.softuni.zooarchitect.service.HabitatService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/animals")
public class AnimalController {
    private final AnimalService animalService;

    private final HabitatService habitatService;

    public AnimalController(AnimalService animalService, HabitatService habitatService) {
        this.animalService = animalService;
        this.habitatService = habitatService;
    }

    @GetMapping("")
    public String viewAnimals(Model model) {
        model.addAttribute("animalList", animalService.getAllAnimals());
        return "animals/animals";
    }

    @GetMapping("/create")
    public String viewCreateAnimal(Model model)
    {
        model.addAttribute("animalDTO", new AnimalCreationDTO());
        return "animals/create";
    }

    @PostMapping("/create")
    public String createAnimal(
            @Valid @ModelAttribute("animalDTO") AnimalCreationDTO animalCreationDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "animals/create";
        }

        animalService.save(animalCreationDTO);

        return "redirect:/animals";
    }

    @GetMapping("/{id}/habitat")
    @Transactional
    public String viewAnimalHabitat(@PathVariable long id, Model model) {
        Animal animal = animalService.getAnimalById(id);
        if (!animal.hasHabitat()) {
            return "error";
        }

        model.addAttribute("animal", animal);
        model.addAttribute("habitat", animal.getHabitat());

        return "animals/habitat";
    }

    @GetMapping("/{id}/habitat/select")
    @Transactional
    public String viewSelectHabitat(@PathVariable long id, Model model) {
        Animal animal = animalService.getAnimalById(id);
        model.addAttribute("animalId", animal.getId());
        model.addAttribute("animalName", animal.getName());
        model.addAttribute("habitatList", habitatService.getAll());

        return "animals/habitat-select";
    }

    @PostMapping("/{animalId}/habitat/select/{habitatId}")
    @Transactional
    public String selectHabitat(@PathVariable long habitatId, @PathVariable long animalId) {
        animalService.addHabitatToAnimal(habitatId, animalId);
        //noinspection SpringMVCViewInspection
        return "redirect:/animals/" + animalId + "/habitat";
    }
}
