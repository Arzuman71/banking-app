package org.example.bankingapp.service;

import lombok.RequiredArgsConstructor;
import org.example.bankingapp.dto.AccountRequestDto;
import org.example.bankingapp.dto.AccountResponseDto;
import org.example.bankingapp.exception.IncorrectValueException;
import org.example.bankingapp.exception.NotEnoughBalanceException;
import org.example.bankingapp.exception.UserNotFoundException;
import org.example.bankingapp.model.User;
import org.example.bankingapp.repository.UserRepository;
import org.example.bankingapp.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final UserRepository userRepository;

    @Transactional
    public AccountResponseDto transferMoney(CustomUserDetails currentUser, AccountRequestDto accountRequestDto) {
        Long senderId = currentUser.getUser().getId();
        Long receiverId = accountRequestDto.getTransferTo();
        BigDecimal transferValue = accountRequestDto.getValue();

        logger.info("Initiating transfer: senderId={}, receiverId={}, amount={}", senderId, receiverId, transferValue);

        Optional<User> senderOpt = userRepository.findById(senderId);
        Optional<User> receiverOpt = userRepository.findById(receiverId);

        if (senderOpt.isEmpty() || receiverOpt.isEmpty() || senderId.equals(receiverId)) {
            logger.warn("Transfer failed: user not found or sender and receiver are the same. senderId={}, receiverId={}", senderId, receiverId);
            throw new UserNotFoundException("User not found");
        }

        if (transferValue.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Transfer failed: invalid transfer value. value={}", transferValue);
            throw new IncorrectValueException("Your value is lower or equals 0");
        }

        User sender = senderOpt.get();
        User receiver = receiverOpt.get();

        if (sender.getAccount().getBalance().compareTo(transferValue) < 0) {
            logger.warn("Transfer failed: not enough balance. senderBalance={}, transferValue={}", sender.getAccount().getBalance(), transferValue);
            throw new NotEnoughBalanceException("You are not enough balance");
        }

        sender.getAccount().setBalance(sender.getAccount().getBalance().subtract(transferValue));
        receiver.getAccount().setBalance(receiver.getAccount().getBalance().add(transferValue));

        logger.info("Transfer successful: senderId={}, newBalance={}", senderId, sender.getAccount().getBalance());

        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setValue(sender.getAccount().getBalance());
        return accountResponseDto;
    }
}