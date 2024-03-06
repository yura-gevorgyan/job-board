package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.UserRole;
import am.itspace.jobboard.exception.EmailIsPresentException;
import am.itspace.jobboard.exception.EmailNotFoundException;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.repository.UserRepository;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.GenerateTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendMessageService sendMessageService;

    @Override
    public User register(User user, String confirmPassword, UserRole userRole) {
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());

        if (byEmail.isPresent()) {
            throw new EmailIsPresentException();
        } else if (!user.getPassword().equals(confirmPassword)) {
            throw new PasswordNotMuchException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(userRole);
        user.setToken(GenerateTokenUtil.generateToken());
        user.setActivated(false);
        user.setDeleted(false);

        User save = userRepository.save(user);
        sendMessageService.sendEmailConfirmMail(save);

        return save;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }
}
