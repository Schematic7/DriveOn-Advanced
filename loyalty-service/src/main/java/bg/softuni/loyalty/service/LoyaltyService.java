package bg.softuni.loyalty.service;

import bg.softuni.loyalty.model.entity.LoyaltyAccount;
import bg.softuni.loyalty.repository.LoyaltyAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    return repository.save(newAccount);
                });

        int earnedPoints = (repairCost != null && repairCost > 0) ? repairCost.intValue() : 0;

        if (earnedPoints > 0) {
            account.setPoints(account.getPoints() + earnedPoints);
            repository.save(account);
        }

        return account.getPoints();
    }

    public Integer getPoints(String username) {
        return repository.findByUsername(username)
                .map(LoyaltyAccount::getPoints)
                .orElse(0);
    }
}