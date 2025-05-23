package org.example.bankingapp.repository;

import org.example.bankingapp.model.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    Optional<PhoneData> findByIdAndUser_Id(Long id, Long userId);

    List<PhoneData> findAllByUserId(Long userId);

    Optional<PhoneData> findByPhone(String phone);

}
