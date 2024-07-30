package bg.softuni.zooarchitect.service;

import bg.softuni.zooarchitect.model.entity.Role;
import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.enums.UserRoleEnum;
import bg.softuni.zooarchitect.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ZooUserDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ZooUserDetailsService zooUserDetailsService;

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
    void testLoadUserByUsername_UserFound() {
        // Given
        String username = "testUser";
        String password = "password";
        Role role = new Role(); // Assume Role class has a proper constructor
        role.setRole(UserRoleEnum.USER);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(Collections.singleton(role));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        UserDetails result = zooUserDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Given
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When / Then
        UsernameNotFoundException thrownException = assertThrows(UsernameNotFoundException.class,
                () -> zooUserDetailsService.loadUserByUsername(username),
                "Expected loadUserByUsername to throw, but it didn't");

        assertEquals("User with username " + username + " not found!", thrownException.getMessage());
    }
}