package bg.softuni.autoservice.mapper.user;

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
}