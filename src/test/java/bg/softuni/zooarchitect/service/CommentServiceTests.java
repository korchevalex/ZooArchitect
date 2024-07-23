package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.CommentCreationDTO;
import bg.softuni.zooarchitect.model.entity.Comment;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.repository.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ZooService zooService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentService commentService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testSaveComment() {
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO();
        Zoo zoo = new Zoo();
        Comment comment = new Comment();
        User user = new User();

        when(modelMapper.map(any(CommentCreationDTO.class), eq(Comment.class))).thenReturn(comment);
        when(authentication.getName()).thenReturn("username");
        when(userService.findUserByUsername("username")).thenReturn(user);

        commentService.saveComment(commentCreationDTO, zoo);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentArgumentCaptor.capture());
        Comment savedComment = commentArgumentCaptor.getValue();

        assertEquals(user, savedComment.getAuthor());
        assertEquals(zoo, savedComment.getZoo());
        assertEquals(comment, savedComment);
        assertEquals(LocalDateTime.now().getYear(), savedComment.getTime().getYear());  // check time roughly
    }

    @Test
    public void testGetCommentById() {
        Comment comment = new Comment();
        when(commentRepository.getReferenceById(anyLong())).thenReturn(comment);

        Comment result = commentService.getCommentById(1L);

        assertEquals(comment, result);
    }

    @Test
    public void testSaveReply() {
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO();
        Comment originalComment = new Comment();
        Comment reply = new Comment();
        Zoo zoo = new Zoo();
        User user = new User();

        when(modelMapper.map(any(CommentCreationDTO.class), eq(Comment.class))).thenReturn(reply);
        when(commentRepository.getReferenceById(anyLong())).thenReturn(originalComment);
        when(userService.getCurrentUser()).thenReturn(user);
        when(zooService.getZooById(anyLong())).thenReturn(zoo);

        commentService.saveReply(commentCreationDTO, 1L, 1L);

        verify(commentRepository).save(reply);
        verify(commentRepository).save(originalComment);

        assertEquals(user, reply.getAuthor());
        assertEquals(zoo, reply.getZoo());
        assertEquals(LocalDateTime.now().getYear(), reply.getTime().getYear());  // check time roughly
        assertEquals(reply, originalComment.getReplies().get(0));
    }
}