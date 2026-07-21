package bg.softuni.autoservice.service.scheduling;

import bg.softuni.autoservice.model.entity.Appointment;
import bg.softuni.autoservice.model.enums.AppointmentStatus; // Променен импорт!
import bg.softuni.autoservice.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class AppointmentCleanupScheduler {

    private final AppointmentRepository appointmentRepository;

    public AppointmentCleanupScheduler(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cancelOverdueAppointments() {
        List<Appointment> allAppointments = appointmentRepository.findAll();

        List<Appointment> overdue = allAppointments.stream()
                .filter(app -> app.getStatus() == AppointmentStatus.PENDING) // Променено
                .filter(app -> app.getAppointmentDate().isBefore(LocalDateTime.now()))
                .toList();

        for (Appointment appointment : overdue) {
            appointment.setStatus(AppointmentStatus.CANCELLED); // Променено
            appointmentRepository.save(appointment);
        }

        log.info("Cron Job: Cancelled {} overdue appointments.", overdue.size());
    }

    @Scheduled(fixedRate = 3600000)
    public void deleteOldCancelledAppointments() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        List<Appointment> allAppointments = appointmentRepository.findAll();

        List<Appointment> oldCancelled = allAppointments.stream()
                .filter(app -> app.getStatus() == AppointmentStatus.CANCELLED) // Променено
                .filter(app -> app.getAppointmentDate().isBefore(thirtyDaysAgo))
                .toList();

        appointmentRepository.deleteAll(oldCancelled);

        log.info("Fixed Rate Job: Deleted {} old cancelled appointments.", oldCancelled.size());
    }
}