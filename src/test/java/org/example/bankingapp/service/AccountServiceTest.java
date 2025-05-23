package org.example.bankingapp.service;

import org.example.bankingapp.dto.AccountRequestDto;
import org.example.bankingapp.dto.AccountResponseDto;
import org.example.bankingapp.exception.IncorrectValueException;
import org.example.bankingapp.exception.NotEnoughBalanceException;
import org.example.bankingapp.exception.UserNotFoundException;
import org.example.bankingapp.model.Account;
import org.example.bankingapp.model.EmailData;
import org.example.bankingapp.model.User;
import org.example.bankingapp.repository.UserRepository;
import org.example.bankingapp.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void transferMoney_SuccessfulTransfer() {
        // Arrange
        User sender = createUser(1L, "sender@test.com", new BigDecimal("1000"));
        User receiver = createUser(2L, "receiver@test.com", new BigDecimal("500"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        AccountRequestDto request = new AccountRequestDto();
        request.setTransferTo(2L);
        request.setValue(new BigDecimal("300"));

        CustomUserDetails userDetails = new CustomUserDetails(sender);

        // Act
        AccountResponseDto response = accountService.transferMoney(userDetails, request);

        // Assert
        assertEquals(new BigDecimal("700"), sender.getAccount().getBalance());
        assertEquals(new BigDecimal("800"), receiver.getAccount().getBalance());
        assertEquals(new BigDecimal("700"), response.getValue());
        verify(userRepository, times(2)).findById(anyLong());
    }

    @Test
    void transferMoney_UserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        AccountRequestDto request = new AccountRequestDto();
        request.setTransferTo(2L);
        request.setValue(new BigDecimal("100"));

        CustomUserDetails userDetails = new CustomUserDetails(new User(1L));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                accountService.transferMoney(userDetails, request));
    }

    @Test
    void transferMoney_NotEnoughBalance_ShouldThrowException() {
        // Arrange
        User sender = createUser(1L, "sender@test.com", new BigDecimal("100"));
        User receiver = createUser(2L, "receiver@test.com", new BigDecimal("50"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        AccountRequestDto request = new AccountRequestDto();
        request.setTransferTo(2L);
        request.setValue(new BigDecimal("200"));

        CustomUserDetails userDetails = new CustomUserDetails(sender);

        // Act & Assert
        assertThrows(NotEnoughBalanceException.class, () ->
                accountService.transferMoney(userDetails, request));
    }

    @Test
    void transferMoney_SameUser_ShouldThrowException() {
        // Arrange
        User user = createUser(1L, "user@test.com", new BigDecimal("1000"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        AccountRequestDto request = new AccountRequestDto();
        request.setTransferTo(1L);
        request.setValue(new BigDecimal("100"));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                accountService.transferMoney(userDetails, request));
    }

    @Test
    void transferMoney_IncorrectValue_ShouldThrowException() {
        // Arrange
        User sender = createUser(1L, "sender@test.com", new BigDecimal("100"));
        User receiver = createUser(2L, "receiver@test.com", new BigDecimal("50"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        AccountRequestDto request = new AccountRequestDto();
        request.setTransferTo(2L);
        request.setValue(new BigDecimal("-100"));
        CustomUserDetails userDetails = new CustomUserDetails(new User(1L));

        // Act & Assert
        assertThrows(IncorrectValueException.class, () ->
                accountService.transferMoney(userDetails, request));
    }


    private User createUser(Long id, String email, BigDecimal balance) {
        User user = new User();

        user.setId(id);
        user.setEmailData(new EmailData(email));

        Account account = new Account();
        account.setBalance(balance);
        user.setAccount(account);

        return user;
    }
}