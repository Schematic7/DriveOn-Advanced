package bg.softuni.loyalty.service;

import bg.softuni.loyalty.exceptions.AccountNotFoundException;
import bg.softuni.loyalty.model.entity.LoyaltyAccount;
import bg.softuni.loyalty.repository.LoyaltyAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoyaltyServiceTest {

    @Mock
    private LoyaltyAccountRepository mockRepository;

    @InjectMocks
    private LoyaltyService loyaltyService;

    private LoyaltyAccount testAccount;

    @BeforeEach
    void setUp() {

        testAccount = new LoyaltyAccount();
        testAccount.setUsername("pesho");
        testAccount.setPoints(100);
    }

    @Test
    void testSpendPoints_Success_DecreasesPoints() {

        when(mockRepository.findByUsername("pesho"))
                .thenReturn(Optional.of(testAccount));

        loyaltyService.spendPoints("pesho", 40);

        assertEquals(60, testAccount.getPoints());

        verify(mockRepository, times(1)).save(testAccount);
    }

    @Test
    void testSpendPoints_AccountNotFound_ThrowsException() {

        when(mockRepository.findByUsername("invalid_user"))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            loyaltyService.spendPoints("invalid_user", 40);
        });

        verify(mockRepository, never()).save(any());
    }

    @Test
    void testSpendPoints_InsufficientPoints_ThrowsIllegalArgumentException() {

        when(mockRepository.findByUsername("pesho"))
                .thenReturn(Optional.of(testAccount));

        assertThrows(IllegalArgumentException.class, () -> {
            loyaltyService.spendPoints("pesho", 150);
        });
    }
}