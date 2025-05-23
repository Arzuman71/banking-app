package org.example.bankingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RefreshTokenDto {
    private String refreshToken;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserChangeDto {

        private Long id;
        private String name;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;
    }
}