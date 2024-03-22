package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.security.SpringUser;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.AddMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SendMailService sendMailService;
    private final PasswordEncoder passwordEncoder;


    //ToDo We'll look at it later

    @GetMapping("/profile")
    public String employerProfile() {
        return "/profile/user-profile";
    }

    @GetMapping("/delete/account")
    public String deleteAccount(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            springUser.getUser().setDeleted(true);
            return "redirect:/login";
        }
        return "redirect:/";
    }

    @GetMapping("/change/password")
    public String changePasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        return "/profile/candidate-change-password";
    }
}

