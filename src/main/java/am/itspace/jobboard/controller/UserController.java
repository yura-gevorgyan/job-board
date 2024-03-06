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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/login/page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/confirm/email/page")
    public String confirmEmailPage() {
        return "confirm-email-page";
    }

    @GetMapping("/confirm/email/change/for/password/page")
    public String confirmEmailForPasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        addMessageToModel(msg, modelMap);
        return "confirm-email-page-for-change-password";
    }

    @GetMapping("/forgot/password/page")
    public String forgotPasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        addMessageToModel(msg, modelMap);
        return "forgot-password";
    }

    @GetMapping("/change/password/page")
    public String changePasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        addMessageToModel(msg, modelMap);
        return "change-password";
    }

    //We'll look at it later
    @GetMapping("/login/success")
    public String loginSuccess() {
        return "redirect:/";
    }

    @GetMapping("/register/page")
    public String registerPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        addMessageToModel(msg, modelMap);

        //Add all user roles except ADMIN
        Set<UserRole> nonAdminRoles = EnumSet.complementOf(EnumSet.of(UserRole.ADMIN));
        modelMap.addAttribute("userRoles", new ArrayList<>(nonAdminRoles));
        return "registration";
    }

    @PostMapping("/confirm/email")
    public String confirmEmail(@RequestParam String confirmEmailCode) {
        Optional<User> optionalUser = userService.findByToken(confirmEmailCode);
        optionalUser.ifPresent(user -> {
            user.setActivated(true);
            userService.save(user);
            sendMessageService.send(user.getEmail(), "successMessage", "Email confirmed successfully!");
        });
        return "redirect:/";
    }

    @PostMapping("/confirm/email/for/change/password")
    public String confirmEmailForChangePassword(@RequestParam String confirmEmailCode, HttpSession httpSession) {
        Optional<User> optionalUser = userService.findByToken(confirmEmailCode);
        if (optionalUser.isPresent()) {
            httpSession.setAttribute("user", optionalUser.get());
            return "redirect:/change/password/page";
        }
        return "redirect:/confirm/email/change/for/password/page?msg=Invalid Email, please try again!";
    }

    @PostMapping("/forgot/password")
    public String forgotPassword(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user != null) {
            user.setToken(GenerateTokenUtil.generateToken());
            userService.save(user);
            sendMessageService.sendEmailConfirmMail(user);
            return "redirect:/confirm/email/change/for/password/page";
        }
        return "redirect:/forgot/password/page?msg=Invalid Email, please try again!";
    }

    @PostMapping("/change/password")
    public String changePassword(@RequestParam String password, String confirmPassword,HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (!password.equals(confirmPassword)) {
            return "redirect:/change/password/page?msg=Password do not much";
        } else if (password.equals(user.getPassword())) {
            return "redirect:/change/password/page?msg=Dont use old password";
        }

        user.setPassword(passwordEncoder.encode(password));
        userService.save(user);
        sendMessageService.send(user.getEmail(), "Changing Password", "Password successfully changed!");
        return "redirect:/login/page";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute User user,
            @RequestParam String confirmPassword) {
        try {
            userService.register(user, confirmPassword, user.getUserRole());
        } catch (EmailIsPresentException e) {
            return "redirect:/register/page?msg=Email is already in use";
        } catch (PasswordNotMuchException e) {
            return "redirect:/register/page?msg=Passwords do not match";
        }
        return "redirect:/confirm/email/page";
    }

    private void addMessageToModel(String msg, ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
    }
}
