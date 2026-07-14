package bg.softuni.autoservice.model.dto.appointment;

import bg.softuni.autoservice.model.enums.AppointmentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppointmentViewDTO {
    private String id;
    private LocalDateTime appointmentDate;
    private String vehicleInfo;
    private String serviceName;
    private AppointmentStatus status;
    private String notes;
}