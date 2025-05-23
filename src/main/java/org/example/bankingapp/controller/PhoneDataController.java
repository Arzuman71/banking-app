package org.example.bankingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bankingapp.dto.EmailDataDto;
import org.example.bankingapp.dto.PhoneDataDto;
import org.example.bankingapp.model.PhoneData;
import org.example.bankingapp.security.CustomUserDetails;
import org.example.bankingapp.service.PhoneDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/phoneData")
public class PhoneDataController {

    private final PhoneDataService phoneDataService;

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePhoneData(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable Long id) {
        phoneDataService.delete(currentUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<PhoneDataDto> updatePhoneData(@AuthenticationPrincipal CustomUserDetails currentUser, @RequestBody PhoneDataDto phoneDataDto) {
        PhoneDataDto update = phoneDataService.update(currentUser, phoneDataDto);
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

    @PostMapping("")
    public ResponseEntity<List<PhoneDataDto>> addPhoneData(@AuthenticationPrincipal CustomUserDetails currentUser, @RequestBody PhoneDataDto phoneDataDto) {
        List<PhoneDataDto> phoneDataDtos = phoneDataService.addPhoneData(currentUser, phoneDataDto);
        return ResponseEntity.status(HttpStatus.OK).body(phoneDataDtos);
    }

}
