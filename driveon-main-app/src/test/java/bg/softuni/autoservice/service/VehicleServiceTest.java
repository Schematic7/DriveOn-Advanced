package bg.softuni.autoservice.service;

import bg.softuni.autoservice.exceptions.DuplicateResourceException;
import bg.softuni.autoservice.exceptions.ResourceNotFoundException;
import bg.softuni.autoservice.exceptions.UnauthorizedActionException;
import bg.softuni.autoservice.model.dto.vehicle.VehicleAddDTO;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.entity.Vehicle;
import bg.softuni.autoservice.repository.UserRepository;
import bg.softuni.autoservice.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private User testUser;
    private Vehicle testVehicle;
    private VehicleAddDTO vehicleAddDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("test_owner");

        testVehicle = new Vehicle();
        testVehicle.setId(UUID.randomUUID());
        testVehicle.setOwner(testUser);
        testVehicle.setVin("123456789");

        vehicleAddDTO = new VehicleAddDTO();
        vehicleAddDTO.setVin("123456789");
        vehicleAddDTO.setLicensePlate("CB1234AB");
        vehicleAddDTO.setMake("Toyota");
        vehicleAddDTO.setModel("Corolla");
        vehicleAddDTO.setYear(2020);
    }

    @Test
    void testAddVehicle_Success() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test_owner");

        when(vehicleRepository.existsByVin(anyString())).thenReturn(false);
        when(vehicleRepository.existsByLicensePlate(anyString())).thenReturn(false);
        when(userRepository.findByUsername("test_owner")).thenReturn(Optional.of(testUser));

        vehicleService.addVehicle(vehicleAddDTO, userDetails);

        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void testAddVehicle_ThrowsDuplicateResourceException() {
        UserDetails userDetails = mock(UserDetails.class);

        when(vehicleRepository.existsByVin(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> vehicleService.addVehicle(vehicleAddDTO, userDetails));

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void testDeleteVehicle_Success() {
        String vehicleId = testVehicle.getId().toString();
        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));

        vehicleService.deleteVehicle(vehicleId, "test_owner");

        verify(vehicleRepository, times(1)).delete(testVehicle);
    }

    @Test
    void testDeleteVehicle_ThrowsUnauthorizedActionException() {
        String vehicleId = testVehicle.getId().toString();
        when(vehicleRepository.findById(testVehicle.getId())).thenReturn(Optional.of(testVehicle));

        assertThrows(UnauthorizedActionException.class,
                () -> vehicleService.deleteVehicle(vehicleId, "wrong_user"));

        verify(vehicleRepository, never()).delete(any(Vehicle.class));
    }

    @Test
    void testGetVehiclesForUser_ThrowsResourceNotFoundException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.getVehiclesForUser("unknown"));
    }
}