package bg.softuni.autoservice.repository;

import bg.softuni.autoservice.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findAllByVehicleOwnerUsernameOrderByAppointmentDateDesc(String username);

    List<Appointment> findAllByOrderByAppointmentDateDesc();
}
