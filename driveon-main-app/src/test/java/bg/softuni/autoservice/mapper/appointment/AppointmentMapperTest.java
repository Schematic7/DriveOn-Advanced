package bg.softuni.autoservice.mapper.appointment;

import bg.softuni.autoservice.model.dto.appointment.AppointmentViewDTO;
import bg.softuni.autoservice.model.entity.Appointment;
import bg.softuni.autoservice.model.entity.ServiceType;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.entity.Vehicle;
import bg.softuni.autoservice.model.enums.AppointmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentMapperTest {

    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        User owner = new User();
        owner.setUsername("ivan_dr");

        Vehicle vehicle = new Vehicle();
        vehicle.setMake("BMW");
        vehicle.setModel("X5");
        vehicle.setLicensePlate("CB5555XX");
        vehicle.setOwner(owner);

        ServiceType serviceType = new ServiceType();
        serviceType.setName("Oil change");

        testAppointment = new Appointment();
        testAppointment.setId(UUID.randomUUID());
        testAppointment.setAppointmentDate(LocalDateTime.of(2027, 1, 1, 10, 0));
        testAppointment.setVehicle(vehicle);
        testAppointment.setServiceType(serviceType);
        testAppointment.setStatus(AppointmentStatus.PENDING);
        testAppointment.setNotes("Check the filter as well");
    }

    @Test
    void testToUserViewDTO_WithValidAppointment() {
        AppointmentViewDTO dto = AppointmentMapper.toUserViewDTO(testAppointment);

        assertNotNull(dto);
        assertEquals(testAppointment.getId().toString(), dto.getId());
        assertEquals(testAppointment.getAppointmentDate(), dto.getAppointmentDate());
        assertEquals("BMW X5 (CB5555XX)", dto.getVehicleInfo());
        assertEquals("Oil change", dto.getServiceName());
        assertEquals(AppointmentStatus.PENDING, dto.getStatus());
    }

    @Test
    void testToUserViewDTO_WithNullAppointment() {
        assertNull(AppointmentMapper.toUserViewDTO(null));
    }

    @Test
    void testToAdminViewDTO_WithValidAppointment() {
        AppointmentViewDTO dto = AppointmentMapper.toAdminViewDTO(testAppointment);

        assertNotNull(dto);
        assertEquals(testAppointment.getId().toString(), dto.getId());
        assertEquals(testAppointment.getAppointmentDate(), dto.getAppointmentDate());
        assertEquals("ivan_dr - BMW CB5555XX", dto.getVehicleInfo());
        assertEquals("Oil change", dto.getServiceName());
        assertEquals(AppointmentStatus.PENDING, dto.getStatus());
        assertEquals("Check the filter as well", dto.getNotes());
    }

    @Test
    void testToAdminViewDTO_WithNullAppointment() {
        assertNull(AppointmentMapper.toAdminViewDTO(null));
    }
}