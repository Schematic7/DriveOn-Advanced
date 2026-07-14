package bg.softuni.autoservice.mapper.vehicle;

import bg.softuni.autoservice.model.dto.vehicle.VehicleAddDTO;
import bg.softuni.autoservice.model.dto.vehicle.VehicleEditDTO;
import bg.softuni.autoservice.model.dto.vehicle.VehicleViewDTO;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.entity.Vehicle;

public class VehicleMapper {
    public static VehicleViewDTO toViewDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        return VehicleViewDTO.builder()
                .id(String.valueOf(vehicle.getId()))
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .licensePlate(vehicle.getLicensePlate())
                .year(vehicle.getYear())
                .vin(vehicle.getVin())
                .build();
    }

    public static Vehicle toVehicleEntity(VehicleAddDTO dto, User owner) {
        if (dto == null) {
            return null;
        }

        return Vehicle.builder()
                .make(dto.getMake())
                .model(dto.getModel())
                .licensePlate(dto.getLicensePlate().toUpperCase())
                .year(dto.getYear())
                .vin(dto.getVin().toUpperCase())
                .owner(owner)
                .build();
    }

    public static void updateVehicleFromDto(Vehicle vehicle, VehicleEditDTO editDTO) {
        if (vehicle == null || editDTO == null) {
            return;
        }

        vehicle.setMake(editDTO.getMake());
        vehicle.setModel(editDTO.getModel());
        vehicle.setLicensePlate(editDTO.getLicensePlate().toUpperCase());
        vehicle.setYear(editDTO.getYear());
        vehicle.setVin(editDTO.getVin().toUpperCase());
    }

    public static VehicleEditDTO toEditDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        VehicleEditDTO editDTO = new VehicleEditDTO();
        editDTO.setId(String.valueOf(vehicle.getId()));
        editDTO.setMake(vehicle.getMake());
        editDTO.setModel(vehicle.getModel());
        editDTO.setLicensePlate(vehicle.getLicensePlate());
        editDTO.setYear(vehicle.getYear());
        editDTO.setVin(vehicle.getVin());

        return editDTO;
    }
}
