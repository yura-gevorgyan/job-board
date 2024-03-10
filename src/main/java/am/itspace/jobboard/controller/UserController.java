package am.itspace.jobboard.controller;

import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.service.impl.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SendMessageService sendMessageService;
    private final PasswordEncoder passwordEncoder;


    //ToDo We'll look at it later

}
