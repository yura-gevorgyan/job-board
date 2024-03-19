package am.itspace.jobboard.controller.advice;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.security.SpringUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {

    @ModelAttribute("currentUser")
    public User cuurentUser(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            return springUser.getUser();
        }
        return null;
    }
}
