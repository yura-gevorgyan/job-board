package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.exception.UseOldPasswordException;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.AddMessageUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forgot/password")
public class ForgotPasswordController {

    private final UserService userService;

    @GetMapping
    public String forgotPasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        return "forgot-password";
    }

    @GetMapping("/confirm")
    public String confirmEmailChangePasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        return "confirm-email-page-for-change-password";
    }

    @GetMapping("/confirm/change")
    public String changePasswordPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        AddMessageUtil.addMessageToModel(msg, modelMap);
        return "change-password";
    }

    @PostMapping
    public String forgotPassword(@RequestParam String email) {
        if (userService.forgotPassword(email) != null) {
            return "redirect:/forgot/password/confirm";
        }
        return "redirect:/forgot/password?msg=Invalid Email, please try again!";
    }

    @PostMapping("/confirm")
    public String confirmEmailChangePassword(@RequestParam String confirmEmailCode, HttpSession httpSession) {
        Optional<User> optionalUser = userService.findByToken(confirmEmailCode);
        if (optionalUser.isPresent()) {
            httpSession.setAttribute("user", optionalUser.get());
            return "redirect:/forgot/password/confirm/change";
        }
        return "redirect:/forgot/password/confirm?msg=Wrong confirm code. Please try again !!!";
    }

    @PostMapping("/confirm/change")
    public String changePassword(@RequestParam String password, String confirmPassword, HttpSession session) {
        User user = (User) session.getAttribute("user");
        try {
            userService.changePassword(password, confirmPassword, user);
        } catch (PasswordNotMuchException e) {
            return "redirect:/forgot/password/confirm/change?msg=Invalid password";
        } catch (UseOldPasswordException e) {
            return "redirect:/forgot/password/confirm/change?msg=You are use old password";
        }
        return "redirect:/login";
    }
}
