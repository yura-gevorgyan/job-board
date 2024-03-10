package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.UserRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user, String confirmPassword, UserRole userRole);

    User save(User user);

    User update(User user);

    User findByEmail(String email);

    User confirmEmail(String confirmEmailCode);

    User forgotPassword(String email);

    User changePassword(String password, String confirmPassword, User user);

    Optional<User> findByToken(String token);

    List<User> findUserByActivated(boolean isActive);
    void delete(User user);
}
