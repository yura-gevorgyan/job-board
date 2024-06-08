package am.itspace.jobboard.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.exception.EmailIsPresentException;
import am.itspace.jobboard.repository.UserRepository;
import am.itspace.jobboard.service.SendMailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SendMailService sendMailService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterExistingUserNotDeleted() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(Role.JOB_SEEKER);
        String confirmPassword = "password123";
        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));

        EmailIsPresentException exception = assertThrows(EmailIsPresentException.class, () -> {
            userService.register(user, confirmPassword, user.getRole());
        });

        assertNotNull(exception);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(user);
        verify(sendMailService, never()).sendEmailConfirmMail(any());
    }

}
