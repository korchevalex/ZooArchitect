package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.HabitatCreationDTO;
import bg.softuni.zooarchitect.service.HabitatService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/habitats")
public class HabitatController {
    private final HabitatService habitatService;

    public HabitatController(HabitatService habitatService) {
        this.habitatService = habitatService;
    }

    @GetMapping("")
    public String viewHabitats(Model model) {
        model.addAttribute("habitatList", habitatService.getAll());
        return "habitats/habitats";
    }

    @GetMapping("/create")
    public String viewCreateHabitat(Model model)
    {
        model.addAttribute("habitatDTO", new HabitatCreationDTO());
        return "habitats/create";
    }

    @PostMapping("/create")
    public String createHabitat(
            @Valid @ModelAttribute("habitatDTO") HabitatCreationDTO habitatCreationDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "habitats/create";
        }

        habitatService.save(habitatCreationDTO);

        return "redirect:/habitats";
    }

    @DeleteMapping("/{id}")
    public String deleteHabitat(@PathVariable long id) {
        try {
            habitatService.deleteHabitatById(id);
        } catch (Exception e) {
            return "habitat-delete-exception";
        }
        return "redirect:/habitats";
    }
}
