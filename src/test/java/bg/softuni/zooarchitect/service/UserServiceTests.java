package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.dto.UserRegisterDTO;
import bg.softuni.zooarchitect.model.entity.Role;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.repository.RoleRepository;
import bg.softuni.zooarchitect.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSaveUser_Success() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("password");
        User user = new User();
        Role role = new Role();
        role.setName("USER");

        when(modelMapper.map(any(UserRegisterDTO.class), eq(User.class))).thenReturn(user);
        when(passwordEncoder.encode(userRegisterDTO.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        userService.save(userRegisterDTO);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());
    }

    @Test
    void testSaveUser_PasswordMismatch() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("differentPassword");

        boolean result = userService.save(userRegisterDTO);

        assertFalse(result);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testSaveUser_RoleNotFound() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("password");

        User user = new User();
        user.setUsername("testUser");

        when(modelMapper.map(userRegisterDTO, User.class)).thenReturn(user);
        when(passwordEncoder.encode(userRegisterDTO.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> userService.save(userRegisterDTO),
                "Expected save to throw, but it didn't");

        assertEquals("Default role not found", thrownException.getMessage());
    }

    @Test
    void testFindUserByUsername_UserFound() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.findUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindUserByUsername_UserNotFound() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException thrownException = assertThrows(UsernameNotFoundException.class,
                () -> userService.findUserByUsername(username),
                "Expected findUserByUsername to throw, but it didn't");

        assertEquals("User not found with username: " + username, thrownException.getMessage());
    }

    @Test
    void testGetCurrentUser() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testUsernameIsTaken() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new User()));

        boolean result = userService.usernameIsTaken(userRegisterDTO);

        assertTrue(result);
    }

    @Test
    void testEmailIsTaken() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        boolean result = userService.emailIsTaken(userRegisterDTO);

        assertTrue(result);
    }
}
