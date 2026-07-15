package bg.softuni.loyalty.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoyaltyPointsResponseDTO {
    private String username;
    private Integer points;
}
