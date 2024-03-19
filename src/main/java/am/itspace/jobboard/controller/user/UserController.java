package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SendMailService sendMailService;
    private final PasswordEncoder passwordEncoder;


    //ToDo We'll look at it later

    @GetMapping("/change/password")
    public String changePasswordPage() {
        return "candidate-change-password";
    }

}
