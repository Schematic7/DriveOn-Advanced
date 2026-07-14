package bg.softuni.autoservice.service;

import bg.softuni.autoservice.exceptions.DuplicateResourceException;
import bg.softuni.autoservice.exceptions.ResourceNotFoundException;
import bg.softuni.autoservice.exceptions.UnauthorizedActionException;
import bg.softuni.autoservice.mapper.vehicle.VehicleMapper;
import bg.softuni.autoservice.model.dto.vehicle.VehicleAddDTO;
import bg.softuni.autoservice.model.dto.vehicle.VehicleEditDTO;
import bg.softuni.autoservice.model.dto.vehicle.VehicleViewDTO;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.entity.Vehicle;
import bg.softuni.autoservice.repository.UserRepository;
import bg.softuni.autoservice.repository.VehicleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public void addVehicle(VehicleAddDTO vehicleAddDTO, UserDetails userDetails) {

        if (vehicleRepository.existsByVin(vehicleAddDTO.getVin()) ||
                vehicleRepository.existsByLicensePlate(vehicleAddDTO.getLicensePlate())) {
            throw new DuplicateResourceException("Vehicle with this VIN or License Plate already exists!");
        }

        User owner = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found in DB!"));

        Vehicle vehicle = VehicleMapper.toVehicleEntity(vehicleAddDTO, owner);

        vehicleRepository.save(vehicle);
    }

    public List<VehicleViewDTO> getVehiclesForUser(String username) {

        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Vehicle> userVehicles = vehicleRepository.findAllByOwner(owner);

        return userVehicles.stream()
                .map(VehicleMapper::toViewDTO)
                .toList();
    }

    public void deleteVehicle(String vehicleId, String username) {

        UUID id = UUID.fromString(vehicleId);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with this ID was not found!"));

        if (!vehicle.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedActionException("You are not authorized to delete this vehicle!");
        }

        vehicleRepository.delete(vehicle);
    }

    public VehicleEditDTO getVehicleForEdit(String vehicleId, String username) {
        UUID id = UUID.fromString(vehicleId);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with this ID was not found!"));

        if (!vehicle.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedActionException("You are not authorized to edit this vehicle!");
        }
        return VehicleMapper.toEditDTO(vehicle);
    }

    public void updateVehicle(String vehicleId, VehicleEditDTO editDTO, String username) {
        UUID id = UUID.fromString(vehicleId);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with this ID was not found!"));

        if (!vehicle.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedActionException("You are not authorized to edit this vehicle!");
        }

        VehicleMapper.updateVehicleFromDto(vehicle, editDTO);

        vehicleRepository.save(vehicle);
    }
}