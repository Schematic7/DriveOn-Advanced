package bg.softuni.autoservice.mapper.user;

import bg.softuni.autoservice.model.dto.user.UserManagementViewDTO;
import bg.softuni.autoservice.model.dto.user.UserProfileDTO;
import bg.softuni.autoservice.model.dto.user.UserRegisterDTO;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.enums.UserRole;

public class UserMapper {

    public static User toUserEntity(UserRegisterDTO registerDTO, String encodedPassword) {
        if (registerDTO == null) {
            return null;
        }

        return User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(encodedPassword)
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .phoneNumber(registerDTO.getPhoneNumber())
                .role(UserRole.USER)
                .build();
    }

    public static UserProfileDTO toUserProfileDTO(User user) {
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setUsername(user.getUsername());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setPhoneNumber(user.getPhoneNumber());
        return profileDTO;
    }

    public static UserManagementViewDTO toUserManagementViewDTO(User user, int loyaltyPoints) {
        UserManagementViewDTO dto = new UserManagementViewDTO();

        dto.setId(user.getId().toString());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        int vehiclesCount = user.getVehicles() != null ? user.getVehicles().size() : 0;
        dto.setVehiclesCount(vehiclesCount);

        int appointmentsCount = 0;
        if (user.getVehicles() != null) {
            appointmentsCount = user.getVehicles().stream()
                    .mapToInt(v -> v.getAppointments() != null ? v.getAppointments().size() : 0)
                    .sum();
        }
        dto.setAppointmentsCount(appointmentsCount);

        dto.setLoyaltyPoints(loyaltyPoints);
        dto.setRole(user.getRole().name());

        return dto;
    }
}