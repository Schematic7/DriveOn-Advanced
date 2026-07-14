package bg.softuni.autoservice.service;

import bg.softuni.autoservice.exceptions.EmailAlreadyExistsException;
import bg.softuni.autoservice.mapper.user.UserMapper;
import bg.softuni.autoservice.model.dto.user.UserRegisterDTO;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
}
