package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.CommentCreationDTO;
import bg.softuni.zooarchitect.model.dto.ZooDTO;
import bg.softuni.zooarchitect.service.CommentService;
import bg.softuni.zooarchitect.service.ZooService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/zoos/{id}/comments")
public class CommentController {
    private final ZooService zooService;
    private final CommentService commentService;

    public CommentController(ZooService zooService, CommentService commentService) {
        this.zooService = zooService;
        this.commentService = commentService;
    }

    @GetMapping("")
    public String viewComments(@PathVariable long id, Model model) {
        ZooDTO zooDTO = zooService.getZooDTOById(id);
        model.addAttribute("zoo", zooDTO);
        model.addAttribute("comments", zooDTO.getComments());
        model.addAttribute("commentDTO", new CommentCreationDTO());
        return "zoo-comments";
    }

    @PostMapping("/create")
    public String createComment(
            @PathVariable long id,
            @Valid @ModelAttribute("commentDTO") CommentCreationDTO commentCreationDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //noinspection SpringMVCViewInspection
            return "redirect:/zoos/" + id + "/comments";
        }
        commentService.saveComment(commentCreationDTO, id);
        //noinspection SpringMVCViewInspection
        return "redirect:/zoos/" + id + "/comments";
    }
}
