package bg.softuni.zooarchitect.controller;

import bg.softuni.zooarchitect.model.dto.AnimalCreationDTO;
import bg.softuni.zooarchitect.model.entity.Animal;
import bg.softuni.zooarchitect.model.entity.Habitat;
import bg.softuni.zooarchitect.service.AnimalService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AnimalControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @MockBean
    private HabitatService habitatService;

    @Test
    @WithMockUser(username = "user")
    public void testViewAnimals() throws Exception {
        when(animalService.getAllAnimals()).thenReturn(List.of(new Animal()));

        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(view().name("animals/animals"))
                .andExpect(model().attributeExists("animalList"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testViewCreateAnimal() throws Exception {
        mockMvc.perform(get("/animals/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("animals/create"))
                .andExpect(model().attributeExists("animalDTO"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testCreateAnimal() throws Exception {
        mockMvc.perform(post("/animals/create").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Lion")
                        .param("imageURL", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Lion_waiting_in_Namibia.jpg/1200px-Lion_waiting_in_Namibia.jpg")
                        .param("description", "Lions are cool"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/animals"));

        verify(animalService, times(1)).save(any(AnimalCreationDTO.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void testViewAnimalHabitat() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Lion");
        animal.setHabitat(new Habitat());
        when(animalService.getAnimalById(anyLong())).thenReturn(animal);

        mockMvc.perform(get("/animals/1/habitat"))
                .andExpect(status().isOk())
                .andExpect(view().name("animals/habitat"))
                .andExpect(model().attributeExists("animal"))
                .andExpect(model().attributeExists("habitat"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testViewAnimalHabitatNoHabitat() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Lion");
        when(animalService.getAnimalById(anyLong())).thenReturn(animal);

        mockMvc.perform(get("/animals/1/habitat"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testViewSelectHabitat() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Lion");
        when(animalService.getAnimalById(anyLong())).thenReturn(animal);
        when(habitatService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/animals/1/habitat/select"))
                .andExpect(status().isOk())
                .andExpect(view().name("animals/habitat-select"))
                .andExpect(model().attributeExists("animalId"))
                .andExpect(model().attributeExists("animalName"))
                .andExpect(model().attributeExists("habitatList"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testSelectHabitat() throws Exception {
        mockMvc.perform(post("/animals/1/habitat/select/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/animals/1/habitat"));

        verify(animalService, times(1)).addHabitatToAnimal(1L, 1L);
    }
}

