package org.example.bankingapp.repository;

import org.example.bankingapp.model.EmailData;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    Optional<EmailData> findByEmail(String email);

    Optional<EmailData> findByIdAndUser_Id(Long id, Long userId);

    List<EmailData> findAllByUserId(Long userId);
}

