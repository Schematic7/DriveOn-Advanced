package bg.softuni.autoservice.service;

import bg.softuni.autoservice.exceptions.EmailAlreadyExistsException;
import bg.softuni.autoservice.exceptions.ResourceNotFoundException;
import bg.softuni.autoservice.mapper.user.UserMapper;
import bg.softuni.autoservice.model.dto.user.UserManagementViewDTO;
import bg.softuni.autoservice.model.dto.user.UserProfileDTO;
import bg.softuni.autoservice.model.dto.user.UserRegisterDTO;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.repository.UserRepository;
import bg.softuni.autoservice.service.loyalty.LoyaltyIntegrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import bg.softuni.autoservice.model.enums.UserRole;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoyaltyIntegrationService loyaltyIntegrationService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, LoyaltyIntegrationService loyaltyIntegrationService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loyaltyIntegrationService = loyaltyIntegrationService;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserRegisterDTO registerDTO) {

        userRepository.findByUsername(registerDTO.getUsername())
                .ifPresent(user -> {
                    throw new RuntimeException("User with this username already exists!");
                });

        userRepository.findByEmail(registerDTO.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException("User with this email already exists!");
                });

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        User userEntity = UserMapper.toUserEntity(registerDTO, encodedPassword);

        userRepository.save(userEntity);
    }

    public UserProfileDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toUserProfileDTO(user);
    }

    public void updateProfile(String username, UserProfileDTO profileDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setPhoneNumber(profileDTO.getPhoneNumber());

        userRepository.save(user);
    }

    public List<UserManagementViewDTO> getAllUsersForManagement() {
        List<User> allUsers = userRepository.findAll();

        return allUsers.stream().map(user -> {

            int points = loyaltyIntegrationService.getAvailablePoints(user.getUsername());

            return UserMapper.toUserManagementViewDTO(user, points);
        }).toList();
    }

    public void changeUserRole(UUID userId, String newRoleStr) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        UserRole newRole = UserRole.valueOf(newRoleStr.toUpperCase());

        user.setRole(newRole);
        userRepository.save(user);
    }
}
