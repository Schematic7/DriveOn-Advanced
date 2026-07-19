package bg.softuni.autoservice.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters!")
    private String username;

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters.")
    private String lastName;

    @NotBlank(message = "Phone number is required.")
    @Size(min = 9, max = 15, message = "Please enter a valid phone number.")
    private String phoneNumber;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Must be a valid email address!")
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 5, message = "Password must be at least 5 characters long!")
    private String password;

    @NotBlank(message = "Confirm password cannot be empty!")
    private String confirmPassword;
}