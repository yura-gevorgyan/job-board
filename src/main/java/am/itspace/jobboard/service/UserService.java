package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    User register(User user, String confirmPassword, UserRole userRole);
    User save(User user);
    User update(User user) throws IOException;
    User findByEmail(String email);
    Optional<User> findByToken(String token);
}
