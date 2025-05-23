package org.example.bankingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bankingapp.dto.*;
import org.example.bankingapp.model.User;
import org.example.bankingapp.security.CustomUserDetails;
import org.example.bankingapp.security.JwtService;
import org.example.bankingapp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;



    @PostMapping("/auth")
    public ResponseEntity<Object> authenticate(@RequestBody AuthRequestDto authRequest) {
            JwtAuthenticationDto jwtAuthenticationDto = userService.authenticate(authRequest);
            return ResponseEntity.ok(jwtAuthenticationDto);
    }

    @PutMapping("")
    public ResponseEntity<UserChangeDto> change(@AuthenticationPrincipal CustomUserDetails currentUser, @RequestBody UserChangeDto userDto) {
        UserChangeDto userDto1 = userService.save(currentUser, userDto);
        return ResponseEntity.ok(userDto1);
    }

    @PostMapping("/search")
    public Page<UserSearchResultDto> search(@RequestBody UserSearchRequest userSearchRequest) {
        return userService.searchUsers(userSearchRequest);
    }


}
