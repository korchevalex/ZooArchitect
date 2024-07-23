package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.CommentDTO;
import bg.softuni.zooarchitect.model.dto.ZooCreationDTO;
import bg.softuni.zooarchitect.model.dto.ZooDTO;
import bg.softuni.zooarchitect.model.entity.Animal;
import bg.softuni.zooarchitect.model.entity.Comment;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.repository.ZooRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class ZooServiceTests {

    @Mock
    private ZooRepository zooRepository;

    @Mock
    private AnimalService animalService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ZooService zooService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSaveZoo() {
        ZooCreationDTO zooCreationDTO = new ZooCreationDTO();
        User user = new User();
        Zoo zoo = new Zoo();

        when(userService.getCurrentUser()).thenReturn(user);
        when(modelMapper.map(zooCreationDTO, Zoo.class)).thenReturn(zoo);

        zooService.save(zooCreationDTO);

        verify(zooRepository, times(1)).save(zoo);
        assertEquals(user, zoo.getOwner());
    }

    @Test
    void testGetZooById() {
        long id = 1L;
        Zoo zoo = new Zoo();

        when(zooRepository.getReferenceById(id)).thenReturn(zoo);

        Zoo result = zooService.getZooById(id);

        assertEquals(zoo, result);
    }

    @Test
    void testGetAllZoos() {
        List<Zoo> zoos = new ArrayList<>();

        when(zooRepository.findAll()).thenReturn(zoos);

        List<Zoo> result = zooService.getAllZoos();

        assertEquals(zoos, result);
    }

    @Test
    void testZooExists_True() {
        User user = new User();
        user.setZoo(new Zoo());

        when(userService.getCurrentUser()).thenReturn(user);

        assertTrue(zooService.zooExists());
    }

    @Test
    void testZooExists_False() {
        User user = new User();

        when(userService.getCurrentUser()).thenReturn(user);

        assertFalse(zooService.zooExists());
    }

    @Test
    @Transactional
    void testDeleteZoo_Success() {
        long id = 1L;
        User user = new User();
        Zoo zoo = new Zoo();
        zoo.setOwner(user);

        when(zooRepository.getReferenceById(id)).thenReturn(zoo);
        when(userService.getCurrentUser()).thenReturn(user);

        boolean result = zooService.delete(id);

        assertTrue(result);
        verify(zooRepository, times(1)).delete(zoo);
    }

    @Test
    @Transactional
    void testDeleteZoo_Failure() {
        long id = 1L;
        User currentUser = new User();
        User otherUser = new User();
        Zoo zoo = new Zoo();
        zoo.setOwner(otherUser);

        when(zooRepository.getReferenceById(id)).thenReturn(zoo);
        when(userService.getCurrentUser()).thenReturn(currentUser);

        boolean result = zooService.delete(id);

        assertFalse(result);
        verify(zooRepository, times(0)).delete(zoo);
    }

    @Test
    @Transactional
    void testAddAnimalToZoo() {
        long animalId = 1L;
        long zooId = 2L;
        Zoo zoo = new Zoo();
        Animal animal = new Animal();

        when(zooRepository.getReferenceById(zooId)).thenReturn(zoo);
        when(animalService.getAnimalById(animalId)).thenReturn(animal);

        zooService.addAnimalToZoo(animalId, zooId);

        verify(zooRepository, times(1)).save(zoo);
        assertTrue(zoo.getAnimals().contains(animal));
    }

    @Test
    @Transactional
    void testGetZooDTOById() {
        long id = 1L;
        Zoo zoo = new Zoo();
        Comment comment = new Comment();
        CommentDTO commentDTO = new CommentDTO();
        List<Comment> comments = List.of(comment);
        List<CommentDTO> commentDTOList = List.of(commentDTO);
        ZooDTO zooDTO = new ZooDTO();

        zoo.setComments(comments);

        when(zooRepository.getReferenceById(id)).thenReturn(zoo);
        when(modelMapper.map(zoo, ZooDTO.class)).thenReturn(zooDTO);
        when(modelMapper.map(zoo.getComments().get(0), CommentDTO.class)).thenReturn(commentDTO);

        ZooDTO result = zooService.getZooDTOById(id);

        assertEquals(zooDTO, result);
        assertEquals(commentDTOList, result.getComments());
    }
}
