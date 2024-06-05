package am.itspace.jobboard.controller.admin.endpoint;

import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersEndpoint {

    private final UserService userService;

    @PostMapping("/block/{userId}")
    public ResponseEntity<Void> blockUser(@PathVariable int userId) {
        userService.blockById(userId);
        log.info("User by {} id, was blocked by Admin", userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/unlock/{userId}")
    public ResponseEntity<Void> unlockUser(@PathVariable int userId) {
        userService.unlockById(userId);
        log.info("User by {} id, was unlocked by Admin", userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
