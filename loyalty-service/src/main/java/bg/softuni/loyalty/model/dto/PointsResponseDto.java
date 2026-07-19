package bg.softuni.loyalty.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointsResponseDto {

    private String username;
    private Integer totalPoints;

}
