package bg.softuni.autoservice.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDTO {
    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters!")
    private String username;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Must be a valid email address!")
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 5, message = "Password must be at least 5 characters long!")
    private String password;

    @NotBlank(message = "Confirm password cannot be empty!")
    private String confirmPassword;
}