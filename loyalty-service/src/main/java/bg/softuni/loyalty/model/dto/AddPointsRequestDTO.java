package bg.softuni.loyalty.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPointsRequestDTO {
    private String username;
    private Double repairCost;
}
