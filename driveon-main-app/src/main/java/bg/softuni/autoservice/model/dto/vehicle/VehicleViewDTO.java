package bg.softuni.autoservice.model.dto.vehicle;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VehicleViewDTO {
    private String id;
    private String make;
    private String model;
    private String licensePlate;
    private Integer year;
    private String vin;
}