package bg.softuni.autoservice.service.client;

import bg.softuni.autoservice.model.dto.loyalty.AddPointsRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "loyalty-client", url = "http://localhost:8081/api/loyalty")
public interface LoyaltyClient {

    String X_API_KEY = "X-API-Key";

    @PostMapping("/add")
    ResponseEntity<String> addPoints(
            @RequestBody AddPointsRequestDto request,
            @RequestHeader(X_API_KEY) String apiKey
    );
}
