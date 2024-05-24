package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class UserControllerAdvice {

    private final UserService userService;

    @ModelAttribute("currentUser")
    public User cuurentUser(@AuthenticationPrincipal SpringUser springUser,
                            @AuthenticationPrincipal OAuth2User oauth2User) {
        String email = null;
        if (springUser != null) {
            email = springUser.getUsername();

        } else if (oauth2User != null) {
            email = oauth2User.getAttribute("email");
        }

        if (email != null) {
            return userService.findByEmail(email);
        }
        return null;
    }
}
