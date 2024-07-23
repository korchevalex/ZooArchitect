package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.CommentCreationDTO;
import bg.softuni.zooarchitect.model.dto.CommentDTO;
import bg.softuni.zooarchitect.model.entity.Comment;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ZooService zooService;

    public CommentService(CommentRepository commentRepository, UserService userService, ModelMapper modelMapper, ZooService zooService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.zooService = zooService;
    }

    public void saveComment(CommentCreationDTO commentCreationDTO, long id) {
        Comment comment = modelMapper.map(commentCreationDTO, Comment.class);
        Zoo zoo = zooService.getZooById(id);
        User user = userService.getCurrentUser();
        comment.setTime(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setZoo(zoo);
        commentRepository.save(comment);
    }

    public Comment getCommentById(long commentId) {
        return commentRepository.getReferenceById(commentId);
    }

    @Transactional
    public CommentDTO getCommentDTOById(long commentId) {
        Comment comment = commentRepository.getReferenceById(commentId);
        return modelMapper.map(comment, CommentDTO.class);
    }
}
