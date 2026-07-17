package bg.softuni.autoservice.model.dto.loyalty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPointsRequestDto {

    private String username;
    private Double repairCost;

}
