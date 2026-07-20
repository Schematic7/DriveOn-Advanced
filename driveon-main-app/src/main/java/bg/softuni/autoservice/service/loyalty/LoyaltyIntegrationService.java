package bg.softuni.autoservice.service.loyalty;


import bg.softuni.autoservice.model.dto.loyalty.SpendPointsRequestDto;
import bg.softuni.autoservice.service.loyalty.client.LoyaltyClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoyaltyIntegrationService {

    private final LoyaltyClient loyaltyClient;
    private final String loyaltyApiKey;

    public LoyaltyIntegrationService(LoyaltyClient loyaltyClient,
                                     @Value("${loyalty.api.key}") String loyaltyApiKey) {
        this.loyaltyClient = loyaltyClient;
        this.loyaltyApiKey = loyaltyApiKey;
    }

    public Integer getAvailablePoints(String username) {
        try {
            return loyaltyClient.getUserPoints(username, loyaltyApiKey).getTotalPoints();
        } catch (Exception e) {
            return 0;
        }
    }

    public void spendPoints(String username, Integer points) {
        try {
            SpendPointsRequestDto request = new SpendPointsRequestDto(username, points);
            loyaltyClient.spendPoints(request, loyaltyApiKey);
            log.info("Successfully spent {} points for user: {}", points, username);
        } catch (Exception e) {
            log.error("Failed to spend loyalty points for user: {}. Error: {}", username, e.getMessage());
        }
    }
}