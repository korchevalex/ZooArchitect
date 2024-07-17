package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.HabitatCreationDTO;
import bg.softuni.zooarchitect.model.entity.Habitat;
import bg.softuni.zooarchitect.repository.HabitatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitatService {
    private final HabitatRepository habitatRepository;

    private final ModelMapper modelMapper;

    public HabitatService(HabitatRepository habitatRepository, ModelMapper modelMapper) {
        this.habitatRepository = habitatRepository;
        this.modelMapper = modelMapper;
    }

    public List<Habitat> getAll() {
        return habitatRepository.findAll();
    }

    public void save(HabitatCreationDTO habitatCreationDTO) {
        Habitat habitat = modelMapper.map(habitatCreationDTO, Habitat.class);
        habitatRepository.save(habitat);
    }

    public Habitat getHabitatById(long habitatId) {
        return habitatRepository.getReferenceById(habitatId);
    }
}
