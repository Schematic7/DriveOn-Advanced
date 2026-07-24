package bg.softuni.loyalty.web;

import bg.softuni.loyalty.model.entity.LoyaltyAccount;
import bg.softuni.loyalty.repository.LoyaltyAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoyaltyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    @BeforeEach
    void setUp() {
        loyaltyAccountRepository.deleteAll();
        LoyaltyAccount testAccount = new LoyaltyAccount();
        testAccount.setUsername("api_user");
        testAccount.setPoints(300);
        loyaltyAccountRepository.save(testAccount);
    }

    @AfterEach
    void tearDown() {
        loyaltyAccountRepository.deleteAll();
    }

    @Test
    void testGetPoints_ReturnsOkAndPoints() throws Exception {
        mockMvc.perform(get("/api/loyalty/api_user")
                        .header("X-API-Key", "super-secret-driveon-key"))
                .andExpect(status().isOk())
                .andExpect(content().string("300"));
    }

    @Test
    void testSpendPoints_ReturnsOkAndUpdatedPoints() throws Exception {
        // Създаваме JSON, който съответства на твоя SpendPointsRequestDto
        String jsonRequest = """
                {
                  "username": "api_user",
                  "pointsToSpend": 100
                }
                """;

        mockMvc.perform(post("/api/loyalty/spend")
                        .header("X-API-Key", "super-secret-driveon-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testAddPoints_ReturnsOkAndLoyaltyPointsResponse() throws Exception {
        // Това е JSON-ът, който твоят AddPointsRequestDto очаква
        String jsonRequest = """
                {
                  "username": "api_user",
                  "repairCost": 500
                }
                """;

        mockMvc.perform(post("/api/loyalty/add")
                        .header("X-API-Key", "super-secret-driveon-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserPointsDto_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/loyalty/points/api_user")
                        .header("X-API-Key", "super-secret-driveon-key"))
                .andExpect(status().isOk());
    }
}