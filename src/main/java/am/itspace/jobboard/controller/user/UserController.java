package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> findUsersHavingChatRoomWith(@AuthenticationPrincipal SpringUser springUser) {
        return ResponseEntity.ok(userService.findUserFriendsHavingChatWith(springUser.getUser().getId()));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null && springUser.getUser() != null) {
            return ResponseEntity.ok(springUser.getUser());
        }
        return ResponseEntity.notFound().build();
    }

}