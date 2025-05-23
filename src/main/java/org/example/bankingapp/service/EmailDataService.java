package org.example.bankingapp.service;

import org.example.bankingapp.dto.EmailDataDto;
import org.example.bankingapp.exception.EmailAlreadyExistException;
import org.example.bankingapp.exception.EmailNotFoundException;
import org.example.bankingapp.model.EmailData;
import org.example.bankingapp.repository.EmailDataRepository;
import org.example.bankingapp.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailDataService {

    private static final Logger logger = LoggerFactory.getLogger(EmailDataService.class);

    private final EmailDataRepository emailDataRepository;

    public EmailDataService(EmailDataRepository emailDataRepository) {
        this.emailDataRepository = emailDataRepository;
    }

    public EmailData findByEmail(String email) {
        logger.info("Searching for email: {}", email);
        return emailDataRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Email not found: {}", email);
                    return new EmailNotFoundException("Email not found");
                });
    }

    public void delete(CustomUserDetails currentUser, Long id) {
        Long userId = currentUser.getUser().getId();
        logger.info("Attempting to delete email with id={} for userId={}", id, userId);

        Optional<EmailData> emailDataByUserId = emailDataRepository.findByIdAndUser_Id(id, userId);

        if (emailDataByUserId.isPresent()) {
            List<EmailData> allByUserId = emailDataRepository.findAllByUserId(userId);
            if (allByUserId.size() >= 2) {
                emailDataRepository.deleteById(id);
                logger.info("Email with id={} deleted for userId={}", id, userId);
            } else {
                logger.warn("Cannot delete email with id={} for userId={} because it's the only one", id, userId);
            }
        } else {
            logger.warn("Email with id={} not found for userId={}", id, userId);
            throw new EmailNotFoundException("Email not found");
        }
    }


    public EmailDataDto update(CustomUserDetails currentUser, EmailDataDto emailDataDto) {
        Long userId = currentUser.getUser().getId();
        logger.info("Updating email id={} for userId={}", emailDataDto.getId(), userId);

        Optional<EmailData> byEmail = emailDataRepository.findByEmail(emailDataDto.getEmail());

        if (byEmail.isPresent()) {
            logger.warn("Email already exists: {}", emailDataDto.getEmail());
            throw new EmailAlreadyExistException("Email Already Exist");
        }
        Optional<EmailData> emailData = emailDataRepository.findByIdAndUser_Id(emailDataDto.getId(), userId);

        if (emailData.isPresent()) {
            emailData.get().setEmail(emailDataDto.getEmail());
            emailDataRepository.save(emailData.get());
            logger.info("Email updated successfully: id={}, newEmail={}", emailDataDto.getId(), emailDataDto.getEmail());
            return emailDataDto;
        } else {
            logger.warn("Email with id={} not found for update by userId={}", emailDataDto.getId(), userId);
            throw new EmailNotFoundException("Email not found");
        }
    }

    public List<EmailDataDto> addEmailData(CustomUserDetails currentUser, EmailDataDto emailDataDto) {
        Long userId = currentUser.getUser().getId();
        String newEmail = emailDataDto.getEmail();

        logger.info("Attempting to add new email for userId={}: {}", userId, newEmail);

        Optional<EmailData> byEmail = emailDataRepository.findByEmail(newEmail);

        if (byEmail.isPresent()) {
            logger.warn("Email already exists: {}", newEmail);
            throw new EmailAlreadyExistException("Email Already Exist");
        }

        EmailData emailData = new EmailData(newEmail, currentUser.getUser());
        emailDataRepository.save(emailData);
        logger.info("New email saved: {} for userId={}", newEmail, userId);

        List<EmailData> allByUserId = emailDataRepository.findAllByUserId(userId);
        logger.debug("Fetched {} email records for userId={}", allByUserId.size(), userId);

        List<EmailDataDto> emailDataDtos = new ArrayList<>();
        for (EmailData ed : allByUserId) {
            logger.debug("EmailData record -> id: {}, email: {}", ed.getId(), ed.getEmail());
            emailDataDtos.add(EmailDataDto.builder()
                    .id(ed.getId())
                    .email(ed.getEmail())
                    .build());
        }

        logger.info("Total emails for userId={} after addition: {}", userId, emailDataDtos.size());
        return emailDataDtos;
    }

}
