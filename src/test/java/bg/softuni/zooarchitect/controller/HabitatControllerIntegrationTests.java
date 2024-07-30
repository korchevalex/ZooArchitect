package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.HabitatCreationDTO;
import bg.softuni.zooarchitect.model.entity.Habitat;
import bg.softuni.zooarchitect.service.HabitatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HabitatControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HabitatService habitatService;

    @Test
    @WithMockUser(username = "user")
    public void testViewHabitats() throws Exception {
        List<Habitat> expectedHabitats = List.of(new Habitat());
        when(habitatService.getAll()).thenReturn(expectedHabitats);

        mockMvc.perform(get("/habitats"))
                .andExpect(status().isOk())
                .andExpect(view().name("habitats/habitats"))
                .andExpect(model().attributeExists("habitatList"))
                .andExpect(model().attribute("habitatList", expectedHabitats));
    }

    @Test
    @WithMockUser(username = "user")
    public void testViewCreateHabitat() throws Exception {
        mockMvc.perform(get("/habitats/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("habitats/create"))
                .andExpect(model().attributeExists("habitatDTO"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testCreateHabitat() throws Exception {
        mockMvc.perform(post("/habitats/create").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Desert")
                        .param("imageURL", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Lion_waiting_in_Namibia.jpg/1200px-Lion_waiting_in_Namibia.jpg")
                        .param("description", "Deserts are cool")
                        .param("latitude", "0")
                        .param("longitude", "0")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/habitats"));

        verify(habitatService, times(1)).save(any(HabitatCreationDTO.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void testDeleteHabitatSuccess() throws Exception {
        doNothing().when(habitatService).deleteHabitatById(1L);

        mockMvc.perform(delete("/habitats/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/habitats"));

        verify(habitatService, times(1)).deleteHabitatById(1L);
    }
}
