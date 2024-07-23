package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.CommentCreationDTO;
import bg.softuni.zooarchitect.model.dto.CommentDTO;
import bg.softuni.zooarchitect.model.entity.Comment;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ZooService zooService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, ZooService zooService, UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.zooService = zooService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void saveComment(CommentCreationDTO commentCreationDTO, Zoo zoo) {
        Comment comment = modelMapper.map(commentCreationDTO, Comment.class);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        comment.setTime(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setZoo(zoo);
        commentRepository.save(comment);
    }

    public Comment getCommentById(long commentId) {
        return commentRepository.getReferenceById(commentId);
    }

    public void saveReply(CommentCreationDTO commentCreationDTO, long id, long comment_id) {
        Comment reply = modelMapper.map(commentCreationDTO, Comment.class);
        Comment originalComment = this.getCommentById(comment_id);
        User user = userService.getCurrentUser();
        reply.setAuthor(user);
        reply.setZoo(zooService.getZooById(id));
        reply.setTime(LocalDateTime.now());
        originalComment.getReplies().add(reply);
        commentRepository.save(reply);
        commentRepository.save(originalComment);
    }

    @Transactional
    public CommentDTO getCommentDTOById(long commentId) {
        Comment comment = commentRepository.getReferenceById(commentId);
        List<CommentDTO> replies = comment.getReplies().stream().map(c -> modelMapper.map(c, CommentDTO.class)).toList();
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setReplies(replies);
        return commentDTO;
    }
}
