package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user, String confirmPassword, Role role);

    User save(User user);

    User update(User user);

    User findByEmail(String email);

    User findByIdAndIsActiveTrue(int id);

    int getUserCount();

    Page<User> findAllUsers(int index);

    Page<User> findAllUsers(Specification<User> specification, int index);

    User confirmEmail(String confirmEmailCode);

    User forgotPassword(String email);

    void changePassword(String password, String confirmPassword, User user);

    Optional<User> findByToken(String token);

    List<User> findUserByActivated(boolean isActive);

    List<User> findUserByPasswordAndIsActivatedFalse(String password);

    void delete(User user);

    List<User> getLast4Users();

    void blockById(int id);

    void unlockById(int id);

    List<User> findUserFriendsHavingChatWith(int currentUserId);

    User findById(int id);

    void updateOAuth2User(User user);

    void deleteProfileCode(User user);

    void deleteProfile(User user);

    User confirmEmailForDelete(String confirmEmailCode);
}
