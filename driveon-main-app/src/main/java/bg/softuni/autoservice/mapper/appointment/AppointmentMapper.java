package bg.softuni.autoservice.mapper.appointment;

import bg.softuni.autoservice.model.dto.appointment.AppointmentViewDTO;
import bg.softuni.autoservice.model.entity.Appointment;

public class AppointmentMapper {

    public static AppointmentViewDTO toUserViewDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        return AppointmentViewDTO.builder()
                .id(appointment.getId().toString())
                .appointmentDate(appointment.getAppointmentDate())
                .vehicleInfo(appointment.getVehicle().getMake() + " " +
                        appointment.getVehicle().getModel() + " (" +
                        appointment.getVehicle().getLicensePlate() + ")")
                .serviceName(appointment.getServiceType().getName())
                .status(appointment.getStatus())
                .build();
    }

    public static AppointmentViewDTO toAdminViewDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        return AppointmentViewDTO.builder()
                .id(appointment.getId().toString())
                .appointmentDate(appointment.getAppointmentDate())
                .vehicleInfo(appointment.getVehicle().getOwner().getUsername() + " - " +
                        appointment.getVehicle().getMake() + " " +
                        appointment.getVehicle().getLicensePlate())
                .serviceName(appointment.getServiceType().getName())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .build();
    }
}
