package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.UserRole;
import am.itspace.jobboard.exception.EmailIsPresentException;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.service.impl.SendMessageService;
import am.itspace.jobboard.util.GenerateTokenUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SendMessageService sendMessageService;
    private final PasswordEncoder passwordEncoder;


    //ToDo We'll look at it later

}
