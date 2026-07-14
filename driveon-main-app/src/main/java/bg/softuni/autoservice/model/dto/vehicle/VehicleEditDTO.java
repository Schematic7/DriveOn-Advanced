package bg.softuni.autoservice.model.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleEditDTO {

    private String id;

    @NotBlank(message = "Make cannot be empty!")
    private String make;

    @NotBlank(message = "Model cannot be empty!")
    private String model;

    @NotBlank(message = "License plate cannot be empty!")
    private String licensePlate;

    @NotNull(message = "Year cannot be empty!")
    private Integer year;

    @NotBlank(message = "VIN cannot be empty!")
    private String vin;
}
