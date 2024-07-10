package bg.softuni.zooarchitect.repository;

import bg.softuni.zooarchitect.model.entity.Zoo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZooRepository extends JpaRepository<Zoo, Long> {
}
