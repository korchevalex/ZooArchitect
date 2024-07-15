package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.CommentCreationDTO;
import bg.softuni.zooarchitect.model.entity.Comment;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.service.CommentService;
import bg.softuni.zooarchitect.service.UserService;
import bg.softuni.zooarchitect.service.ZooService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/zoos/{id}/comments")
public class CommentController {
    private final ZooService zooService;
    private final UserService userService;
    private final CommentService commentService;
    private final ModelMapper modelMapper;

    public CommentController(ZooService zooService, UserService userService, CommentService commentService, ModelMapper modelMapper) {
        this.zooService = zooService;
        this.userService = userService;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    @Transactional
    public String viewComments(@PathVariable long id, Model model) {
        Zoo zoo = zooService.getZooById(id);
        if (zoo == null) {
            throw new IllegalArgumentException("Invalid zoo id");
        }

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
        Comment comment = modelMapper.map(commentCreationDTO, Comment.class);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        comment.setTime(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setZoo(zoo);
        commentService.save(comment);
        //noinspection SpringMVCViewInspection
        return "redirect:/zoos/" + id + "/comments";
    }

    @GetMapping("/{comment_id}")
    @Transactional
    public String viewReplies(@PathVariable long id, Model model, @PathVariable long comment_id) {
        Zoo zoo = zooService.getZooById(id);
        if (zoo == null) {
            throw new IllegalArgumentException("Invalid zoo id");
        }
        Comment comment = commentService.getCommentById(comment_id);
        if (comment == null) {
            throw new IllegalArgumentException("Invalid comment id");
        }
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
        Zoo zoo = zooService.getZooById(id);
        if (zoo == null) {
            throw new IllegalArgumentException("Invalid zoo id");
        }
        if (bindingResult.hasErrors()) {
            //noinspection SpringMVCViewInspection
            return "redirect:/zoos/" + id + "/comments/" + comment_id;
        }
        Comment reply = modelMapper.map(commentCreationDTO, Comment.class);
        Comment originalComment = commentService.getCommentById(comment_id);
        if (originalComment == null) {
            throw new IllegalArgumentException("Invalid comment id");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        reply.setAuthor(user);
        reply.setZoo(zoo);
        reply.setTime(LocalDateTime.now());
        originalComment.getReplies().add(reply);
        commentService.save(reply);
        commentService.save(originalComment);
        //noinspection SpringMVCViewInspection
        return "redirect:/zoos/" + id + "/comments/" + comment_id;
    }
}
