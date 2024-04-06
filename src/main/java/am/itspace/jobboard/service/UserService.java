package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user, String confirmPassword, Role role);

    User save(User user);

    User update(User user);

    User findByEmail(String email);
    User findByIdAndIsActiveTrue(int id);

    int getUserCount();

    int getTotalPages();

    Page<User> getUsersFromNToM(int index);

    int getTotalPagesOfSearch(String email, String role);

    int getUserCountOfEmailRole(String email, String role);

    Page<User> getUsersFromNToMForSearch(int index, String email, String role);
    User confirmEmail(String confirmEmailCode);

    User forgotPassword(String email);

    void changePassword(String password, String confirmPassword, User user);

    Optional<User> findByToken(String token);

    List<User> findUserByActivated(boolean isActive);

    void delete(User user);

    List<User> getLast4Users();

    void blockById(int id);

    void unlockById(int id);
}

