package bg.softuni.autoservice.model.event;

public record AppointmentCreatedEvent(String username, String serviceName) {
}