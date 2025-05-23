package org.example.bankingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequest {
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Integer page = 0;
    private Integer size = 10;
}

