package bg.softuni.autoservice.service.listener;

import bg.softuni.autoservice.model.event.AppointmentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppointmentNotificationListener {

    @EventListener
    public void onAppointmentCreated(AppointmentCreatedEvent event) {

        log.info("Spring Event Triggered: Sending confirmation notification to user '{}' for service '{}'.",
                event.username(), event.serviceName());
    }
}