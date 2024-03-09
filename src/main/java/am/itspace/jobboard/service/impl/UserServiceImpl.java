package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.UserRole;
import am.itspace.jobboard.exception.EmailIsPresentException;
import am.itspace.jobboard.exception.EmailNotFoundException;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.exception.UseOldPasswordException;
import am.itspace.jobboard.repository.UserRepository;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.GenerateTokenUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
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
        user.setRegisterDate(new Date());

        User save = userRepository.save(user);
        sendMessageService.sendEmailConfirmMail(save);

        return save;
    }

    @Override
    public User changePassword(String password, String confirmPassword, User user) {

        if (!password.equals(confirmPassword)) {
            throw new PasswordNotMuchException();
        } else if (passwordEncoder.matches(password, user.getPassword())) {
            throw new UseOldPasswordException();
        }
        user.setPassword(passwordEncoder.encode(password));
        save(user);
        sendMessageService.send(user.getEmail(), "Changing Password", "Password successfully changed!");
        return user;
    }

    @Override
    public User confirmEmail(String confirmEmailCode) {
        Optional<User> optionalUser = findByToken(confirmEmailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActivated(true);
            save(user);
            sendMessageService.send(user.getEmail(), "successMessage", "Email confirmed successfully!");
            return user;
        }
        return null;
    }

    @Override
    public User forgotPassword(String email) {
        User user = findByEmail(email);
        if (user != null) {
            user.setToken(GenerateTokenUtil.generateToken());
            save(user);
            sendMessageService.sendEmailConfirmMail(user);
            return user;
        }
        return null;
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
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }

    @Override
    public List<User> findUserByActivated(boolean isActive) {
        return userRepository.findUserByActivated(isActive);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}
