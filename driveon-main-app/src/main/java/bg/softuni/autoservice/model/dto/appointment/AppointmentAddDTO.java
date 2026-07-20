package bg.softuni.autoservice.model.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentAddDTO {

    @NotBlank(message = "Please select a vehicle!")
    private String vehicleId;

    @NotBlank(message = "Please select a service type!")
    private String serviceTypeId;

    @NotNull(message = "Please select date and time!")
    @Future(message = "The appointment must be in the future!")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime appointmentDate;

    private String notes;

    private Boolean useLoyaltyPoints;
}