package am.itspace.jobboard.controller.admin.endpoint;

import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersEndpoint {

    private final UserService userService;

    @PostMapping("/block/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockUser(@PathVariable int userId) {
        userService.blockById(userId);
    }

    @PostMapping("/unlock/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlockUser(@PathVariable int userId) {
        userService.unlockById(userId);
    }

}
