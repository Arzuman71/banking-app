package org.example.bankingapp.repository;

import org.example.bankingapp.model.EmailData;
import org.example.bankingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {

    Optional<User> findByEmailData(EmailData emailData);
}
