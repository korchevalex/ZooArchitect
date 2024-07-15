package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.CommentCreationDTO;
import bg.softuni.zooarchitect.model.entity.Comment;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.service.CommentService;
import bg.softuni.zooarchitect.service.ZooService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Transactional
    public String viewComments(@PathVariable long id, Model model) {
        Zoo zoo = zooService.getZooById(id);

        List<Comment> comments = zoo.getComments();
        model.addAttribute("zoo", zoo);
        model.addAttribute("comments", comments);
        model.addAttribute("commentDTO", new CommentCreationDTO());
        model.addAttribute("zooOwner", zoo.getOwner());
        return "zoo-comments";
    }

    @PostMapping("/create")
    public String createComment(
            @PathVariable long id,
            @Valid @ModelAttribute("commentDTO") CommentCreationDTO commentCreationDTO,
            BindingResult bindingResult) {
        Zoo zoo = zooService.getZooById(id);
        if (zoo == null) {
            throw new IllegalArgumentException("Invalid zoo id");
        }
        if (bindingResult.hasErrors()) {
            //noinspection SpringMVCViewInspection
            return "redirect:/zoos/" + id + "/comments";
        }
        commentService.saveComment(commentCreationDTO, zoo);
        //noinspection SpringMVCViewInspection
        return "redirect:/zoos/" + id + "/comments";
    }

    @GetMapping("/{comment_id}")
    @Transactional
    public String viewReplies(@PathVariable long id, Model model, @PathVariable long comment_id) {
        Zoo zoo = zooService.getZooById(id);
        Comment comment = commentService.getCommentById(comment_id);
        List<Comment> replies = comment.getReplies();
        model.addAttribute("zoo", zoo);
        model.addAttribute("comment", comment);
        model.addAttribute("replies", replies);
        model.addAttribute("commentDTO", new CommentCreationDTO());
        model.addAttribute("zooOwner", zoo.getOwner());

        return "zoo-comments-replies";
    }

    @PostMapping("/{comment_id}/create")
    @Transactional
    public String createReply(
            @PathVariable long id,
            @Valid @ModelAttribute("commentDTO") CommentCreationDTO commentCreationDTO,
            BindingResult bindingResult,
            @PathVariable long comment_id) {
        if (bindingResult.hasErrors()) {
            //noinspection SpringMVCViewInspection
            return "redirect:/zoos/" + id + "/comments/" + comment_id;
        }
        commentService.saveReply(commentCreationDTO, id, comment_id);
        //noinspection SpringMVCViewInspection
        return "redirect:/zoos/" + id + "/comments/" + comment_id;
    }
}
