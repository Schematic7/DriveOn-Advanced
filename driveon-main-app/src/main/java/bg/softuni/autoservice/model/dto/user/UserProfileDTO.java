package bg.softuni.autoservice.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileDTO {

    private String username;
    private String email;

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters.")
    private String lastName;

    @NotBlank(message = "Phone number is required.")
    @Size(min = 9, max = 15, message = "Please enter a valid phone number.")
    private String phoneNumber;
}