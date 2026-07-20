package bg.softuni.autoservice.service;

import bg.softuni.autoservice.exceptions.ResourceNotFoundException;
import bg.softuni.autoservice.exceptions.UnauthorizedActionException;
import bg.softuni.autoservice.mapper.appointment.AppointmentMapper;
import bg.softuni.autoservice.model.dto.appointment.AppointmentAddDTO;
import bg.softuni.autoservice.model.dto.appointment.AppointmentViewDTO;
import bg.softuni.autoservice.model.dto.loyalty.AddPointsRequestDto;
import bg.softuni.autoservice.model.entity.Appointment;
import bg.softuni.autoservice.model.entity.ServiceType;
import bg.softuni.autoservice.model.entity.Vehicle;
import bg.softuni.autoservice.repository.AppointmentRepository;
import bg.softuni.autoservice.repository.ServiceTypeRepository;
import bg.softuni.autoservice.repository.VehicleRepository;
import bg.softuni.autoservice.service.loyalty.LoyaltyIntegrationService;
import bg.softuni.autoservice.service.loyalty.client.LoyaltyClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final LoyaltyClient loyaltyClient;
    private final LoyaltyIntegrationService loyaltyIntegrationService;

    @Value("${loyalty.api.key}")
    private String apiKey;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              VehicleRepository vehicleRepository,
                              ServiceTypeRepository serviceTypeRepository, LoyaltyClient loyaltyClient, LoyaltyIntegrationService loyaltyIntegrationService) {
        this.appointmentRepository = appointmentRepository;
        this.vehicleRepository = vehicleRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.loyaltyClient = loyaltyClient;
        this.loyaltyIntegrationService = loyaltyIntegrationService;
    }

    public void createAppointment(AppointmentAddDTO dto, String username) {

        Vehicle vehicle = vehicleRepository.findById(UUID.fromString(dto.getVehicleId()))
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found!"));


        if (!vehicle.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedActionException("You are not authorized to book an appointment for this vehicle!");
        }


        ServiceType serviceType = serviceTypeRepository.findById(UUID.fromString(dto.getServiceTypeId()))
                .orElseThrow(() -> new ResourceNotFoundException("Service type not found!"));

        Appointment appointment = Appointment.builder()
                .appointmentDate(dto.getAppointmentDate())
                .notes(dto.getNotes())
                .vehicle(vehicle)
                .serviceType(serviceType)
                .usesLoyaltyPoints(dto.getUseLoyaltyPoints() != null && dto.getUseLoyaltyPoints())
                .build();

        appointmentRepository.save(appointment);
    }

    public List<AppointmentViewDTO> getAppointmentsForUser(String username) {
        return appointmentRepository.findAllByVehicleOwnerUsernameOrderByAppointmentDateDesc(username)
                .stream()
                .map(AppointmentMapper::toUserViewDTO)
                .toList();
    }

    public void cancelAppointment(UUID id, String username) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found!"));

        if (!appointment.getVehicle().getOwner().getUsername().equals(username)) {
            throw new UnauthorizedActionException("You are not authorized to cancel this appointment!");
        }

        appointment.setStatus(bg.softuni.autoservice.model.enums.AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);
    }

    public List<AppointmentViewDTO> getAllAppointmentsForAdmin() {
        return appointmentRepository.findAllByOrderByAppointmentDateDesc()
                .stream()
                .map(AppointmentMapper::toAdminViewDTO)
                .toList();
    }

    public void approveAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found!"));

        appointment.setStatus(bg.softuni.autoservice.model.enums.AppointmentStatus.APPROVED);
        appointmentRepository.save(appointment);
    }

    public void completeAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found!"));

        appointment.setStatus(bg.softuni.autoservice.model.enums.AppointmentStatus.COMPLETED);

        String username = appointment.getVehicle().getOwner().getUsername();

        if (Boolean.TRUE.equals(appointment.getUsesLoyaltyPoints())) {
            loyaltyIntegrationService.spendPoints(username, 20);
        }

        try {
            Double repairCost = appointment.getServiceType().getPrice();

            AddPointsRequestDto requestDto = new AddPointsRequestDto(username, repairCost);
            loyaltyClient.addPoints(requestDto, apiKey);

            log.info("Successfully sent loyalty points to microservice for user: {}", username);

        } catch (Exception e) {
            log.error("Failed to connect to Loyalty Service: {}", e.getMessage());
        }
        appointmentRepository.save(appointment);
    }
}