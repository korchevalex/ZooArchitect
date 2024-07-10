package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import bg.softuni.zooarchitect.repository.ZooRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZooService {
    private final ZooRepository zooRepository;
    private final UserService userService;

    public ZooService(ZooRepository zooRepository, UserService userService) {
        this.zooRepository = zooRepository;
        this.userService = userService;
    }

    public void save(Zoo zoo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        zoo.setOwner(user);
        zooRepository.save(zoo);
    }

    public Zoo getZooById(long id) {
        return zooRepository.getReferenceById(id);
    }

    public List<Zoo> getAllZoos() {
        return zooRepository.findAll();
    }
}
