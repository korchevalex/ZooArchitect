package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.AnimalCreationDTO;
import bg.softuni.zooarchitect.model.entity.Animal;
import bg.softuni.zooarchitect.repository.AnimalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;

    public AnimalService(AnimalRepository animalRepository, ModelMapper modelMapper) {
        this.animalRepository = animalRepository;
        this.modelMapper = modelMapper;
    }

    public void save(AnimalCreationDTO animalCreationDTO) {
        Animal animal = modelMapper.map(animalCreationDTO, Animal.class);
        animalRepository.save(animal);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public Animal getAnimalById(long id) {
        return animalRepository.getReferenceById(id);
    }
}
