package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class UserControllerAdvice {

    private final SecurityService securityService;

    @ModelAttribute("currentUser")
    public User currentUser() {
        return securityService.getCurrentUser();
    }
}
