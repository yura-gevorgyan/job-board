package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.UserRole;
import am.itspace.jobboard.exception.EmailIsPresentException;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.AddMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String registerPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        Set<UserRole> nonAdminRoles = EnumSet.complementOf(EnumSet.of(UserRole.ADMIN));
        modelMap.addAttribute("userRoles", new ArrayList<>(nonAdminRoles));
        return "registration";
    }

    @GetMapping("/confirm")
    public String confirmEmailPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        return "register-confirm";
    }

    @PostMapping("/confirm")
    public String confirmEmail(@RequestParam String confirmEmailCode) {
        if (userService.confirmEmail(confirmEmailCode) != null) {
            return "redirect:/";
        }
        return "redirect:/register/confirm?msg=Invalid confirm code";
    }

    @PostMapping
    public String register(
            @ModelAttribute User user,
            @RequestParam String confirmPassword) {
        try {
            userService.register(user, confirmPassword, user.getUserRole());
        } catch (EmailIsPresentException e) {
            return "redirect:/register?msg=Email is already in use";
        } catch (PasswordNotMuchException e) {
            return "redirect:/register?msg=Passwords do not match";
        }
        return "redirect:/register/confirm";
    }
}
