package bg.softuni.autoservice.service;

import bg.softuni.autoservice.exceptions.ResourceNotFoundException;
import bg.softuni.autoservice.exceptions.UnauthorizedActionException;
import bg.softuni.autoservice.model.dto.appointment.AppointmentAddDTO;
import bg.softuni.autoservice.model.entity.Appointment;
import bg.softuni.autoservice.model.entity.ServiceType;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.entity.Vehicle;
import bg.softuni.autoservice.repository.AppointmentRepository;
import bg.softuni.autoservice.repository.ServiceTypeRepository;
import bg.softuni.autoservice.repository.VehicleRepository;
import bg.softuni.autoservice.service.loyalty.LoyaltyIntegrationService;
import bg.softuni.autoservice.service.loyalty.client.LoyaltyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private ServiceTypeRepository serviceTypeRepository;
    @Mock
    private LoyaltyClient loyaltyClient;
    @Mock
    private LoyaltyIntegrationService loyaltyIntegrationService;

    @InjectMocks
    private AppointmentService appointmentService;

    private User testOwner;
    private Vehicle testVehicle;
    private ServiceType testServiceType;
    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        testOwner = new User();
        testOwner.setUsername("driver_bg");

        testVehicle = new Vehicle();
        testVehicle.setId(UUID.randomUUID());
        testVehicle.setOwner(testOwner);

        testServiceType = new ServiceType();
        testServiceType.setId(UUID.randomUUID());
        testServiceType.setPrice(150.0);

        testAppointment = new Appointment();
        testAppointment.setId(UUID.randomUUID());
        testAppointment.setVehicle(testVehicle);
        testAppointment.setServiceType(testServiceType);
        testAppointment.setUsesLoyaltyPoints(true);
    }

    @Test
    void testCreateAppointment_Success() {
        AppointmentAddDTO dto = new AppointmentAddDTO();
        dto.setVehicleId(testVehicle.getId().toString());
        dto.setServiceTypeId(testServiceType.getId().toString());
        dto.setUseLoyaltyPoints(false);

        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));
        when(serviceTypeRepository.findById(testServiceType.getId())).thenReturn(Optional.of(testServiceType));

        appointmentService.createAppointment(dto, "driver_bg");

        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testCreateAppointment_Unauthorized() {
        AppointmentAddDTO dto = new AppointmentAddDTO();
        dto.setVehicleId(testVehicle.getId().toString());

        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));

        assertThrows(UnauthorizedActionException.class,
                () -> appointmentService.createAppointment(dto, "hacker_user"));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void testCompleteAppointment_SuccessWithLoyaltyPoints() {
        UUID appointmentId = testAppointment.getId();
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));

        appointmentService.completeAppointment(appointmentId);

        verify(loyaltyIntegrationService, times(1)).spendPoints("driver_bg", 20);
        verify(loyaltyClient, times(1)).addPoints(any(), any());
        verify(appointmentRepository, times(1)).save(testAppointment);
    }

    @Test
    void testApproveAppointment_ThrowsNotFound() {
        UUID randomId = UUID.randomUUID();
        when(appointmentRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.approveAppointment(randomId));
    }
}