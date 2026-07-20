package bg.softuni.autoservice.model.dto.loyalty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpendPointsRequestDto {
    private String username;
    private Integer pointsToSpend;
}