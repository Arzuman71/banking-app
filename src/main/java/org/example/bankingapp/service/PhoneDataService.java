package org.example.bankingapp.service;

import org.example.bankingapp.dto.PhoneDataDto;
import org.example.bankingapp.exception.PhoneAlreadyExistException;
import org.example.bankingapp.exception.PhoneNotFoundException;
import org.example.bankingapp.model.PhoneData;
import org.example.bankingapp.repository.PhoneDataRepository;
import org.example.bankingapp.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneDataService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneDataService.class);

    private final PhoneDataRepository phoneDataRepository;

    public PhoneDataService(PhoneDataRepository phoneDataRepository) {
        this.phoneDataRepository = phoneDataRepository;
    }

    public void delete(CustomUserDetails currentUser, Long id) {
        Long userId = currentUser.getUser().getId();
        logger.info("Attempting to delete phone data with id={} for userId={}", id, userId);

        Optional<PhoneData> phoneData = phoneDataRepository.findByIdAndUser_Id(id, userId);
        if (phoneData.isPresent()) {
            List<PhoneData> allByUserId = phoneDataRepository.findAllByUserId(userId);
            if (allByUserId.size() >= 2) {
                phoneDataRepository.deleteById(id);
                logger.info("Phone data with id={} deleted for userId={}", id, userId);
            } else {
                logger.warn("Cannot delete phone data with id={} for userId={} because it's the only one", id, userId);
            }
        } else {
            logger.warn("Phone data with id={} not found for userId={}", id, userId);
            throw new PhoneNotFoundException("Phone not found");
        }
    }

    public PhoneDataDto update(CustomUserDetails currentUser, PhoneDataDto phoneDataDto) {
        Long userId = currentUser.getUser().getId();
        logger.info("Updating phone data id={} for userId={}", phoneDataDto.getId(), userId);

        Optional<PhoneData> byPhone = phoneDataRepository.findByPhone(phoneDataDto.getPhone());
        if (byPhone.isPresent()) {
            throw new PhoneAlreadyExistException("Phone Already Exist");
        }
        Optional<PhoneData> phoneData = phoneDataRepository.findByIdAndUser_Id(phoneDataDto.getId(), userId);
        if (phoneData.isPresent()) {
            phoneData.get().setPhone(phoneDataDto.getPhone());
            phoneDataRepository.save(phoneData.get());
            logger.info("Phone data updated successfully: id={}, newPhone={}", phoneDataDto.getId(), phoneDataDto.getPhone());
            return phoneDataDto;
        } else {
            logger.warn("Phone data with id={} not found for update by userId={}", phoneDataDto.getId(), userId);
            throw new PhoneNotFoundException("Phone not found");
        }
    }

    public List<PhoneDataDto> addPhoneData(CustomUserDetails currentUser, PhoneDataDto phoneDataDto) {
        Long userId = currentUser.getUser().getId();
        logger.info("Adding new phone for userId={}: {}", userId, phoneDataDto.getPhone());

        Optional<PhoneData> byPhone = phoneDataRepository.findByPhone(phoneDataDto.getPhone());
        if (byPhone.isPresent()) {
            throw new PhoneAlreadyExistException("Phone Already Exist");
        }

        PhoneData phoneData = new PhoneData(phoneDataDto.getPhone(), currentUser.getUser());
        phoneDataRepository.save(phoneData);
        logger.info("Phone data saved: {} for userId={}", phoneDataDto.getPhone(), userId);

        List<PhoneData> allByUserId = phoneDataRepository.findAllByUserId(userId);
        logger.debug("Fetched {} phone records for userId={}", allByUserId.size(), userId);

        List<PhoneDataDto> phoneDataDtos = new ArrayList<>();
        for (PhoneData pd : allByUserId) {
            logger.debug("PhoneData record -> id: {}, phone: {}", pd.getId(), pd.getPhone());
            phoneDataDtos.add(PhoneDataDto.builder()
                    .id(pd.getId())
                    .phone(pd.getPhone())
                    .build());
        }

        logger.info("Total phone numbers for userId={} after addition: {}", userId, phoneDataDtos.size());
        return phoneDataDtos;
    }
}
