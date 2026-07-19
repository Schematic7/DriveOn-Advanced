package bg.softuni.autoservice.service.client;

import bg.softuni.autoservice.model.dto.loyalty.AddPointsRequestDto;
import bg.softuni.autoservice.model.dto.loyalty.PointsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "loyalty-client", url = "http://localhost:8081/api/loyalty")
public interface LoyaltyClient {

    String X_API_KEY = "X-API-Key";

    @PostMapping("/add")
    ResponseEntity<String> addPoints(
            @RequestBody AddPointsRequestDto request,
            @RequestHeader(X_API_KEY) String apiKey
    );

    @GetMapping("/points/{username}")
    PointsResponseDto getUserPoints(@PathVariable("username") String username,
                                    @RequestHeader("X-API-Key") String apiKey);

}
