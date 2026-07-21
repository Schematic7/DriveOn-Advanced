package bg.softuni.autoservice.service.scheduling;

import bg.softuni.autoservice.model.entity.Appointment;
import bg.softuni.autoservice.model.enums.AppointmentStatus; // Променен импорт!
import bg.softuni.autoservice.repository.AppointmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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

        System.out.println("Cron Job: Cancelled " + overdue.size() + " overdue appointments.");
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

        System.out.println("Fixed Rate Job: Deleted " + oldCancelled.size() + " old cancelled appointments.");
    }
}