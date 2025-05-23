package org.example.bankingapp.sheduler;

import lombok.RequiredArgsConstructor;
import org.example.bankingapp.model.Account;
import org.example.bankingapp.repository.AccountRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceIncreaseService {
    private final AccountRepository accountRepository;

    private static final BigDecimal INCREASE_PERCENTAGE = new BigDecimal("0.10"); // 10%
    private static final int SCALE = 2; // 2 знака после запятой для денежных значений
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final double MAX_DEPOSIT_PERCENT = 207;
    private static final double ADDED_PERCENT= 10;

    @Scheduled(fixedRate = 30000) // Каждые 30 секунд
    @Transactional
    public void increaseBalances() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            if (account.getAddedDepositPercent() < MAX_DEPOSIT_PERCENT-10) {
                addAccountBalance(account, INCREASE_PERCENTAGE, ADDED_PERCENT);
            } else if (MAX_DEPOSIT_PERCENT - account.getAddedDepositPercent() > 0) {
                double addedPercent = MAX_DEPOSIT_PERCENT - account.getAddedDepositPercent();
                BigDecimal increasePercentage = BigDecimal
                        .valueOf((MAX_DEPOSIT_PERCENT - account.getAddedDepositPercent()) / 100);
                addAccountBalance(account, increasePercentage,addedPercent);
            }
        }
    }

    private void addAccountBalance(Account account, BigDecimal increasePercentage,double addedPercent ) {
        BigDecimal currentBalance = account.getBalance();

        // Проверяем, что баланс не null
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
            account.setBalance(currentBalance);
        }

        // Вычисляем 10% от текущего баланса
        BigDecimal increaseAmount = currentBalance.multiply(increasePercentage)
                .setScale(SCALE, ROUNDING_MODE);

        // Увеличиваем баланс
        BigDecimal newBalance = currentBalance.add(increaseAmount)
                .setScale(SCALE, ROUNDING_MODE);

        account.setBalance(newBalance);
        account.setAddedDepositPercent(account.getAddedDepositPercent() + addedPercent);
        accountRepository.save(account);
    }

}