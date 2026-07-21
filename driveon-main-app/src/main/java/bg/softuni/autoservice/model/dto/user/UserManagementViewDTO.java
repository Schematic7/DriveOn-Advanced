package bg.softuni.autoservice.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementViewDTO {
    private String id;
    private String username;
    private String email;
    private int vehiclesCount;
    private int appointmentsCount;
    private int loyaltyPoints;
    private String role;
}
