package org.example.bankingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bankingapp.dto.AccountRequestDto;
import org.example.bankingapp.dto.AccountResponseDto;
import org.example.bankingapp.security.CustomUserDetails;
import org.example.bankingapp.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/transfer")
    public ResponseEntity<AccountResponseDto> transferMoney(@AuthenticationPrincipal CustomUserDetails currentUser, @RequestBody AccountRequestDto accountRequestDto) {
        AccountResponseDto accountResponseDto = accountService.transferMoney(currentUser, accountRequestDto);
        return ResponseEntity.ok(accountResponseDto);
    }
}
