package bg.softuni.loyalty.service;

import bg.softuni.loyalty.model.entity.LoyaltyAccount;
import bg.softuni.loyalty.repository.LoyaltyAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class LoyaltyServiceIntegrationTest {

    @Autowired
    private LoyaltyService loyaltyService;

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    @BeforeEach
    void setUp() {

        loyaltyAccountRepository.deleteAll();

        LoyaltyAccount account = new LoyaltyAccount();
        account.setUsername("integration_user");
        account.setPoints(200);

        loyaltyAccountRepository.save(account);
    }

    @AfterEach
    void tearDown() {

        loyaltyAccountRepository.deleteAll();
    }

    @Test
    void testSpendPoints_Integration_UpdatesRealDatabase() {

        loyaltyService.spendPoints("integration_user", 50);

        LoyaltyAccount updatedAccount = loyaltyAccountRepository.findByUsername("integration_user").orElse(null);

        assertTrue(updatedAccount != null);
        assertEquals(150, updatedAccount.getPoints());
    }
}