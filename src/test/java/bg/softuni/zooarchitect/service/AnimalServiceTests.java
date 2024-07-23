package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.AnimalCreationDTO;
import bg.softuni.zooarchitect.model.entity.Animal;
import bg.softuni.zooarchitect.model.entity.Habitat;
import bg.softuni.zooarchitect.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTests {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private HabitatService habitatService;

    @Mock
    private ModelMapper modelMapper;

    private AnimalService animalService;

    @BeforeEach
    public void setUp() {
        animalService = new AnimalService(animalRepository, habitatService, modelMapper);
    }

    @Test
    public void testSave() {
        AnimalCreationDTO animalCreationDTO = new AnimalCreationDTO();
        Animal animal = new Animal();

        when(modelMapper.map(any(AnimalCreationDTO.class), eq(Animal.class))).thenReturn(animal);

        animalService.save(animalCreationDTO);

        ArgumentCaptor<Animal> animalArgumentCaptor = ArgumentCaptor.forClass(Animal.class);
        verify(animalRepository).save(animalArgumentCaptor.capture());
        assertEquals(animal, animalArgumentCaptor.getValue());
    }

    @Test
    public void testGetAllAnimals() {
        List<Animal> animals = new ArrayList<>();
        when(animalRepository.findAll()).thenReturn(animals);

        List<Animal> result = animalService.getAllAnimals();

        assertEquals(animals, result);
    }

    @Test
    public void testGetAnimalById() {
        Animal animal = new Animal();
        when(animalRepository.getReferenceById(anyLong())).thenReturn(animal);

        Animal result = animalService.getAnimalById(1L);

        assertEquals(animal, result);
    }

    @Test
    public void testAddHabitatToAnimal() {
        Animal animal = new Animal();
        Habitat habitat = new Habitat();

        when(animalRepository.getReferenceById(anyLong())).thenReturn(animal);
        when(habitatService.getHabitatById(anyLong())).thenReturn(habitat);

        animalService.addHabitatToAnimal(1L, 1L);

        verify(animalRepository).save(animal);
        assertEquals(habitat, animal.getHabitat());
    }
}