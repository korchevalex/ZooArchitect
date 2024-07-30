package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.UserRegisterDTO;
import bg.softuni.zooarchitect.model.entity.Role;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.enums.UserRoleEnum;
import bg.softuni.zooarchitect.model.user.ZooUserDetails;
import bg.softuni.zooarchitect.repository.RoleRepository;
import bg.softuni.zooarchitect.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService  {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public boolean save(UserRegisterDTO userRegisterDTO) {
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            return false;
        }
        User user = modelMapper.map(userRegisterDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> roleOptional = roleRepository.findByRole(UserRoleEnum.USER);
        if (roleOptional.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleOptional.get());
            user.setRoles(roles);
        } else {
            throw new RuntimeException("Default role not found");
        }

        userRepository.save(user);
        return true;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return this.findUserByUsername(auth.getName());
    }

    public boolean usernameIsTaken(UserRegisterDTO userRegisterDTO) {
        return userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent();
    }

    public boolean emailIsTaken(UserRegisterDTO userRegisterDTO) {
        return userRepository.findByEmail(userRegisterDTO.getEmail()).isPresent();
    }

    public Optional<ZooUserDetails> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof ZooUserDetails mobileleZooUserDetails) {
            return Optional.of(mobileleZooUserDetails);
        }
        return Optional.empty();
    }
}
