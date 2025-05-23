package org.example.bankingapp.service;

import lombok.RequiredArgsConstructor;
import org.example.bankingapp.dto.*;
import org.example.bankingapp.exception.PasswordNotMatchException;
import org.example.bankingapp.exception.UserNotFoundException;
import org.example.bankingapp.model.EmailData;
import org.example.bankingapp.model.PhoneData;
import org.example.bankingapp.model.User;
import org.example.bankingapp.repository.UserRepository;
import org.example.bankingapp.repository.specification.UserSpecifications;
import org.example.bankingapp.security.CustomUserDetails;
import org.example.bankingapp.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final EmailDataService emailDataService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService tokenUtil;


    public User findByEmail(String email) {
        logger.info("Attempting to find user by email: {}", email);
        EmailData byEmail = emailDataService.findByEmail(email);
        return userRepository.findByEmailData(byEmail)
                .orElseThrow(() -> {
                    logger.warn("User not found for email: {}", email);
                    return new UserNotFoundException("User not found");
                });
    }

    public UserChangeDto save(CustomUserDetails currentUser, UserChangeDto userDto) {
        User user = currentUser.getUser();
        logger.info("Updating user info for userId={}", user.getId());

        user.userChange(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);

        logger.info("User updated successfully: userId={}, name={}", savedUser.getId(), savedUser.getName());

        UserChangeDto resultDto = new UserChangeDto();
        resultDto.setName(savedUser.getName());
        resultDto.setDateOfBirth(savedUser.getDateOfBirth().toString());
        return resultDto;
    }

    public Page<UserSearchResultDto> searchUsers(
            UserSearchRequest userSearchRequest) {
        logger.info("Searching users with filters: name={}, email={}, phone={}, dob={}",
                userSearchRequest.getName(),
                userSearchRequest.getEmail(),
                userSearchRequest.getPhone(),
                userSearchRequest.getDateOfBirth());

        Specification<User> spec = UserSpecifications.withFilters(
                userSearchRequest.getName(), userSearchRequest.getEmail(), userSearchRequest.getPhone(), userSearchRequest.getDateOfBirth());
        Pageable pageable = PageRequest.of(
                userSearchRequest.getPage(),
                userSearchRequest.getSize()
        );
        Page<User> usersPage = userRepository.findAll(spec, pageable);
        logger.info("Found {} users matching criteria", usersPage.getTotalElements());
        // Преобразуем Page<User> в Page<UserSearchResultDto>
        return usersPage.map(this::convertToDto);
    }


    private UserSearchResultDto convertToDto(User user) {
        UserSearchResultDto dto = new UserSearchResultDto();
        // Копируем основные свойства
        BeanUtils.copyProperties(user, dto);

        // Устанавливаем email из связанной сущности
        if (user.getEmailData() != null) {
            dto.setEmail(user.getEmailData().getEmail());
        }

        // Собираем все телефоны пользователя
        if (user.getPhoneData() != null && !user.getPhoneData().isEmpty()) {
            List<String> phones = user.getPhoneData().stream()
                    .map(PhoneData::getPhone)
                    .collect(Collectors.toList());
            dto.setPhones(phones);
        }
        logger.info("Converted User to DTO: id={}, name={}, email={}, phones={}",
                user.getId(),
                user.getName(),
                dto.getEmail(),
                dto.getPhones());


        return dto;
    }

    public JwtAuthenticationDto authenticate(AuthRequestDto authRequest) {
        logger.info("Attempting authentication for email={}", authRequest.getEmail());

        User user = findByEmail(authRequest.getEmail());
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            logger.info("Authentication successful for userId={} email={}", user.getId(), user.getEmailData().getEmail());
            return tokenUtil.generateAuthToken(user.getEmailData().getEmail());
        }

        logger.warn("Authentication failed: password mismatch for email={}", authRequest.getEmail());
        throw new PasswordNotMatchException("password not match");
    }
}
