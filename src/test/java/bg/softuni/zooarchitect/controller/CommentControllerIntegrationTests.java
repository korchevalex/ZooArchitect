package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.CommentCreationDTO;
import bg.softuni.zooarchitect.model.dto.CommentDTO;
import bg.softuni.zooarchitect.model.dto.ZooDTO;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.service.CommentService;
import bg.softuni.zooarchitect.service.ZooService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerIntegrationTests {

    @MockBean
    private ZooService zooService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user")
    public void testViewComments() throws Exception {
        ZooDTO zooDTO = new ZooDTO();
        zooDTO.setId(1L);
        zooDTO.setName("name");
        zooDTO.setDescription("description");
        zooDTO.setImageURL("imageURL");
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        zooDTO.setOwner(user);
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setText("text");
        commentDTO.setAuthor(user);
        commentDTO.setTime(LocalDateTime.MIN);
        zooDTO.setComments(List.of(commentDTO));
        when(zooService.getZooDTOById(anyLong())).thenReturn(zooDTO);

        mockMvc.perform(get("/zoos/1/comments"))
                .andExpect(status().isOk())
                .andExpect(view().name("zoo-comments"))
                .andExpect(model().attributeExists("zoo"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("commentDTO"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testCreateComment() throws Exception {
        mockMvc.perform(post("/zoos/1/comments/create").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("text", "text"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/zoos/1/comments"));

        verify(commentService, times(1)).saveComment(any(CommentCreationDTO.class), anyLong());
    }
}
