package bg.softuni.loyalty.service;

import bg.softuni.loyalty.exceptions.AccountNotFoundException;
import bg.softuni.loyalty.model.dto.PointsResponseDto;
import bg.softuni.loyalty.model.entity.LoyaltyAccount;
import bg.softuni.loyalty.repository.LoyaltyAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LoyaltyService {

    private final LoyaltyAccountRepository repository;

    public LoyaltyService(LoyaltyAccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Integer addPoints(String username, Double repairCost) {

        LoyaltyAccount account = repository.findByUsername(username)
                .orElseGet(() -> {
                    LoyaltyAccount newAccount = new LoyaltyAccount();
                    newAccount.setUsername(username);
                    newAccount.setPoints(0);
                    log.info("Created new loyalty account for user: {}", username);
                    return repository.save(newAccount);
                });

        int earnedPoints = (repairCost != null && repairCost > 0) ? repairCost.intValue() : 0;

        if (earnedPoints > 0) {
            account.setPoints(account.getPoints() + earnedPoints);
            repository.save(account);

            log.info("Added {} points to user: {}. Total points now: {}", earnedPoints, username, account.getPoints());
        }

        return account.getPoints();
    }

    public Integer getPoints(String username) {
        return repository.findByUsername(username)
                .map(LoyaltyAccount::getPoints)
                .orElse(0);
    }

    @Transactional
    public PointsResponseDto spendPoints(String username, Integer pointsToSpend) {

        LoyaltyAccount account = repository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Attempted to spend points for non-existent account: {}", username);
                    return new AccountNotFoundException("Account not found for user: " + username);
                });

        if (account.getPoints() < pointsToSpend) {
            log.warn("User {} tried to spend {} points, but only has {}", username, pointsToSpend, account.getPoints());
            throw new IllegalArgumentException("Insufficient points! User has: " + account.getPoints());
        }

        account.setPoints(account.getPoints() - pointsToSpend);
        repository.save(account);

        log.info("User {} spent {} points. Remaining points: {}", username, pointsToSpend, account.getPoints());

        return new PointsResponseDto(account.getUsername(), account.getPoints());
    }
}