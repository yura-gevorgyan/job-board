package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.exception.UseOldPasswordException;
import am.itspace.jobboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forgot/password")
public class ForgotPasswordController {

    private final UserService userService;

    @GetMapping
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/confirm")
    public String confirmEmailChangePasswordPage() {
        return "confirm-forgot-password";
    }

    @GetMapping("/confirm/change")
    public String changePasswordPage() {
        return "change-password";
    }

    @PostMapping
    public String forgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        if (userService.forgotPassword(email) != null) {
            return "redirect:/forgot/password/confirm";
        }
        redirectAttributes.addFlashAttribute("msg", "Invalid Email, please try again.");
        return "redirect:/forgot/password";
    }

    @PostMapping("/confirm")
    public String confirmEmailChangePassword(@RequestParam String confirmEmailCode, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userService.findByToken(confirmEmailCode);
        if (optionalUser.isPresent()) {
            httpSession.setAttribute("user", optionalUser.get());
            log.info("User by {} id, are going to changed password", optionalUser.get().getId());
            return "redirect:/forgot/password/confirm/change";
        }
        redirectAttributes.addFlashAttribute("msg", "Wrong confirm code. Please try again.");
        return "redirect:/forgot/password/confirm";
    }

    @PostMapping("/confirm/change")
    public String changePassword(@RequestParam String password, String confirmPassword, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        try {
            userService.changePassword(password, confirmPassword, user);
            log.info("User by {} id, has changed password successfully", user.getId());
        } catch (PasswordNotMuchException e) {
            redirectAttributes.addFlashAttribute("msg", "Invalid password.");
            return "redirect:/forgot/password/confirm/change";

        } catch (UseOldPasswordException e) {
            redirectAttributes.addFlashAttribute("msg", "You are using old password.");
            return "redirect:/forgot/password/confirm/change";
        }
        return "redirect:/login";
    }
}
