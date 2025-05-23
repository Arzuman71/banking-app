package org.example.bankingapp.security;

import lombok.RequiredArgsConstructor;
import org.example.bankingapp.exception.EmailNotFoundException;
import org.example.bankingapp.repository.EmailDataRepository;
import org.example.bankingapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailData(emailDataRepository.findByEmail(username)
                        .orElseThrow(() -> new EmailNotFoundException("Email not found"))).map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}