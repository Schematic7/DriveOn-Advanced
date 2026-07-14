package bg.softuni.autoservice.model.dto.vehicle;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleAddDTO {

    @NotBlank(message = "Make cannot be empty!")
    @Size(min = 2, max = 20, message = "Make must be between 2 and 20 characters!")
    private String make;

    @NotBlank(message = "Model cannot be empty!")
    @Size(min = 1, max = 20, message = "Model must be between 1 and 20 characters!")
    private String model;

    @NotBlank(message = "License plate cannot be empty!")
    @Size(min = 4, max = 10, message = "License plate must be between 4 and 10 characters!")
    private String licensePlate;

    @NotNull(message = "Year is required!")
    @Min(value = 1930, message = "Year must be after 1930!")
    @Max(value = 2026, message = "Year cannot be in the future!")
    private Integer year;

    @NotBlank(message = "VIN cannot be empty!")
    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters!")
    private String vin;
}