package bg.softuni.loyalty.web;

import bg.softuni.loyalty.model.dto.AddPointsRequestDto;
import bg.softuni.loyalty.model.dto.LoyaltyPointsResponseDto;
import bg.softuni.loyalty.model.dto.PointsResponseDto;
import bg.softuni.loyalty.service.LoyaltyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @PostMapping("/add")
    public ResponseEntity<LoyaltyPointsResponseDto> addPoints(@RequestBody AddPointsRequestDto request) {
        Integer totalPoints = loyaltyService.addPoints(request.getUsername(), request.getRepairCost());
        return ResponseEntity.ok(new LoyaltyPointsResponseDto(request.getUsername(), totalPoints));
    }

    @GetMapping("/{username}")
    public ResponseEntity<Integer> getPoints(@PathVariable String username) {
        return ResponseEntity.ok(loyaltyService.getPoints(username));
    }

    @GetMapping("/points/{username}")
    public ResponseEntity<PointsResponseDto> getUserPoints(@PathVariable String username) {

        Integer points = loyaltyService.getPoints(username);

        PointsResponseDto response = new PointsResponseDto(username, points);
        return ResponseEntity.ok(response);
    }
}