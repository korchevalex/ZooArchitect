package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.CommentCreationDTO;
import bg.softuni.zooarchitect.model.dto.CommentDTO;
import bg.softuni.zooarchitect.model.entity.Comment;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ZooService zooService;

    @InjectMocks
    private CommentService commentService;

    private CommentCreationDTO commentCreationDTO;
    private Comment comment;
    private Zoo zoo;
    private User user;

    @BeforeEach
    void setUp() {
        commentCreationDTO = new CommentCreationDTO();
        comment = new Comment();
        zoo = new Zoo();
        user = new User();
    }

    @Test
    void saveComment() {
        when(modelMapper.map(any(CommentCreationDTO.class), eq(Comment.class))).thenReturn(comment);
        when(zooService.getZooById(anyLong())).thenReturn(zoo);
        when(userService.getCurrentUser()).thenReturn(user);
        commentService.saveComment(commentCreationDTO, 1L);

        verify(commentRepository, times(1)).save(comment);
        assertEquals(user, comment.getAuthor());
        assertEquals(zoo, comment.getZoo());
    }

    @Test
    void getCommentById() {
        when(commentRepository.getReferenceById(anyLong())).thenReturn(comment);

        Comment result = commentService.getCommentById(1L);

        verify(commentRepository, times(1)).getReferenceById(1L);
        assertEquals(comment, result);
    }

    @Test
    void getCommentDTOById() {
        CommentDTO commentDTO = new CommentDTO();
        when(commentRepository.getReferenceById(anyLong())).thenReturn(comment);
        when(modelMapper.map(comment, CommentDTO.class)).thenReturn(commentDTO);

        CommentDTO result = commentService.getCommentDTOById(1L);

        verify(commentRepository, times(1)).getReferenceById(1L);
        verify(modelMapper, times(1)).map(comment, CommentDTO.class);
        assertEquals(commentDTO, result);
    }
}