package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> findUsersHavingChatRoomWith() {
        User user = securityService.getCurrentUser();
        return ResponseEntity.ok(userService.findUserFriendsHavingChatWith(user.getId()));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser() {
        User user = securityService.getCurrentUser();
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}
