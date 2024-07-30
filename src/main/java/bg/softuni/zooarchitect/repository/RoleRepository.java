package bg.softuni.zooarchitect.repository;

import bg.softuni.zooarchitect.model.entity.Role;
import bg.softuni.zooarchitect.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(UserRoleEnum role);
}
