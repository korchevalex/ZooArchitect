package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.ZooCreationDTO;
import bg.softuni.zooarchitect.model.entity.Animal;
import bg.softuni.zooarchitect.service.AnimalService;
import bg.softuni.zooarchitect.service.ZooService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ZooControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @MockBean
    private ZooService zooService;

    @Test
    @WithMockUser(username = "user")
    public void testViewZoos() throws Exception {
        when(animalService.getAllAnimals()).thenReturn(List.of(new Animal()));

        mockMvc.perform(get("/zoos"))
                .andExpect(status().isOk())
                .andExpect(view().name("zoos/zoos"))
                .andExpect(model().attribute("zooList", zooService.getAllZoos()))
                .andExpect(model().attribute("zooExists", zooService.zooExists()));
    }

    @Test
    @WithMockUser(username = "user")
    public void testViewCreateZooSuccess() throws Exception {
        mockMvc.perform(get("/zoos/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("zoos/create"))
                .andExpect(model().attributeExists("zooDTO"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testViewCreateZooFailure() throws Exception {
        when(zooService.zooExists()).thenReturn(true);

        mockMvc.perform(get("/zoos/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/zoos"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testCreateZoo() throws Exception {
        mockMvc.perform(post("/zoos/create").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "My Zoo")
                        .param("imageURL", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Lion_waiting_in_Namibia.jpg/1200px-Lion_waiting_in_Namibia.jpg")
                        .param("description", "Zoos are cool"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/zoos"));

        verify(zooService, times(1)).save(any(ZooCreationDTO.class));
    }
}
