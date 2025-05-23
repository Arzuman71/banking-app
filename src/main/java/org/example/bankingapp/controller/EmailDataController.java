package org.example.bankingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bankingapp.dto.EmailDataDto;
import org.example.bankingapp.security.CustomUserDetails;
import org.example.bankingapp.service.EmailDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/emailData")
public class EmailDataController {

    private final EmailDataService emailDataService;

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmailData(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable Long id) {
        emailDataService.delete(currentUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<EmailDataDto> updateEmailData(@AuthenticationPrincipal CustomUserDetails currentUser, @RequestBody EmailDataDto emailDataDto) {
        EmailDataDto update = emailDataService.update(currentUser, emailDataDto);
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

    @PostMapping("")
    public ResponseEntity<List<EmailDataDto>> addEmailData(@AuthenticationPrincipal CustomUserDetails currentUser, @RequestBody EmailDataDto emailDataDto) {
        List<EmailDataDto> emailDataDtos = emailDataService.addEmailData(currentUser, emailDataDto);
        return ResponseEntity.status(HttpStatus.OK).body(emailDataDtos);
    }

}
