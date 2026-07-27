package bg.softuni.autoservice.service;

import bg.softuni.autoservice.exceptions.DuplicateResourceException;
import bg.softuni.autoservice.exceptions.EmailAlreadyExistsException;
import bg.softuni.autoservice.model.dto.user.UserManagementViewDTO;
import bg.softuni.autoservice.model.dto.user.UserProfileDTO;
import bg.softuni.autoservice.model.dto.user.UserRegisterDTO;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.enums.UserRole;
import bg.softuni.autoservice.repository.UserRepository;
import bg.softuni.autoservice.service.loyalty.LoyaltyIntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private LoyaltyIntegrationService loyaltyIntegrationService;

    private User testUser;

    @BeforeEach
    void setUp() {

        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("integration_user");
        testUser.setEmail("integration@test.com");
        testUser.setPassword("hashedpassword123");
        testUser.setFirstName("Ivan");
        testUser.setLastName("Ivanov");
        testUser.setPhoneNumber("0888123456");
        testUser.setRole(UserRole.USER);

        userRepository.save(testUser);
    }

    @Test
    void testRegisterUser_Success() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("new_user");
        dto.setEmail("new_user@test.com");
        dto.setPassword("password123");
        dto.setFirstName("Peter");
        dto.setLastName("Petrov");
        dto.setPhoneNumber("0899123456");

        userService.registerUser(dto);

        User savedUser = userRepository.findByUsername("new_user").orElse(null);
        assertNotNull(savedUser);
        assertEquals("new_user@test.com", savedUser.getEmail());
        assertNotEquals("password123", savedUser.getPassword());
    }

    @Test
    void testRegisterUser_ThrowsEmailAlreadyExists() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("another_user");
        dto.setEmail("integration@test.com");
        dto.setPassword("pass123");
        dto.setPhoneNumber("0899999999");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(dto));
    }

    @Test
    void testRegisterUser_ThrowsDuplicateResourceForPhone() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("another_user2");
        dto.setEmail("unique@test.com");
        dto.setPassword("pass123");
        dto.setPhoneNumber("0888123456");

        assertThrows(DuplicateResourceException.class, () -> userService.registerUser(dto));
    }

    @Test
    void testUpdateProfile_Success() {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setFirstName("UpdatedName");
        dto.setLastName("Ivanov");
        dto.setPhoneNumber("0888654321");

        userService.updateProfile("integration_user", dto);

        User updatedUser = userRepository.findByUsername("integration_user").orElseThrow();
        assertEquals("UpdatedName", updatedUser.getFirstName());
        assertEquals("0888654321", updatedUser.getPhoneNumber());
    }

    @Test
    void testUpdateProfile_ThrowsDuplicateResourceForPhoneNumber() {
        User secondUser = new User();
        secondUser.setUsername("second_user");
        secondUser.setEmail("second@test.com");
        secondUser.setPassword("pass");
        secondUser.setFirstName("Georgi");
        secondUser.setLastName("Georgiev");
        secondUser.setPhoneNumber("0877777777");
        secondUser.setRole(UserRole.USER);

        userRepository.save(secondUser);


        UserProfileDTO dto = new UserProfileDTO();
        dto.setFirstName("Ivan");
        dto.setLastName("Ivanov");
        dto.setPhoneNumber("0877777777");

        assertThrows(DuplicateResourceException.class, () -> userService.updateProfile("integration_user", dto));
    }

    @Test
    void testChangeUserRole_Success() {
        userService.changeUserRole(testUser.getId(), "ADMIN");

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals(UserRole.ADMIN, updatedUser.getRole());
    }

    @Test
    void testGetAllUsersForManagement_Success() {

        when(loyaltyIntegrationService.getAvailablePoints(anyString())).thenReturn(150);

        List<UserManagementViewDTO> users = userService.getAllUsersForManagement();

        assertFalse(users.isEmpty());
        assertEquals(150, users.get(0).getLoyaltyPoints());
    }
}