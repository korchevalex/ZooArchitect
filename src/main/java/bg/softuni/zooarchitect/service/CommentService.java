package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.entity.Comment;
import bg.softuni.zooarchitect.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
