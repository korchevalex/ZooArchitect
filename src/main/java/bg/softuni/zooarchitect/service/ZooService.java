package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.CommentDTO;
import bg.softuni.zooarchitect.model.dto.ZooCreationDTO;
import bg.softuni.zooarchitect.model.dto.ZooDTO;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.repository.ZooRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZooService {
    private final ZooRepository zooRepository;
    private final AnimalService animalService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ZooService(ZooRepository zooRepository, AnimalService animalService, UserService userService, ModelMapper modelMapper) {
        this.zooRepository = zooRepository;
        this.animalService = animalService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void save(ZooCreationDTO zooCreationDTO) {
        User user = userService.getCurrentUser();
        Zoo zoo = modelMapper.map(zooCreationDTO, Zoo.class);
        zoo.setOwner(user);
        zooRepository.save(zoo);
    }

    public Zoo getZooById(long id) {
        return zooRepository.getReferenceById(id);
    }

    public List<Zoo> getAllZoos() {
        return zooRepository.findAll();
    }

    public boolean zooExists() {
        return userService.getCurrentUser().getZoo() != null;
    }

    @Transactional
    public boolean delete(long id) {
        Zoo zoo = zooRepository.getReferenceById(id);
        if (!userService.getCurrentUser().equals(zoo.getOwner())) {
            return false;
        }
        zooRepository.delete(zoo);
        return true;
    }

    @Transactional
    public void addAnimalToZoo(long animalId, long zooId) {
        Zoo zoo = zooRepository.getReferenceById(zooId);
        zoo.getAnimals().add(animalService.getAnimalById(animalId));
        zooRepository.save(zoo);
    }

    @Transactional
    public ZooDTO getZooDTOById(long id) {
        Zoo zoo = zooRepository.getReferenceById(id);
        List<CommentDTO> commentDTOList = zoo.getComments().stream().map(c -> modelMapper.map(c, CommentDTO.class)).toList();
        ZooDTO zooDTO = modelMapper.map(zoo, ZooDTO.class);
        zooDTO.setComments(commentDTOList);
        return zooDTO;
    }

    public void deleteAll() {
        zooRepository.deleteAll();
    }
}
