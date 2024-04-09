package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String registerPage(ModelMap modelMap) {
        Set<Role> nonAdminRoles = EnumSet.complementOf(EnumSet.of(Role.ADMIN));
        modelMap.addAttribute("roles", new ArrayList<>(nonAdminRoles));
        return "registration";
    }

    @GetMapping("/confirm")
    public String confirmEmailPage() {
        return "register-confirm";
    }

    @PostMapping("/confirm")
    public String confirmEmail(@RequestParam String confirmEmailCode, RedirectAttributes redirectAttributes) {
        if (userService.confirmEmail(confirmEmailCode) != null) {
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("msg", "Invalid confirm code.");
        return "redirect:/register/confirm";
    }

    @PostMapping
    public String register(
            @ModelAttribute User user,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (user.getRole() == null || user.getRole().toString().isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Choose your type.");
            return "redirect:/register";
        }

        try {
            userService.register(user, confirmPassword, user.getRole());

        } catch (EmailIsPresentException e) {
            redirectAttributes.addFlashAttribute("msg", "Email is already in use.");
            return "redirect:/register";

        } catch (PasswordNotMuchException e) {
            redirectAttributes.addFlashAttribute("msg", "Passwords do not match.");
            return "redirect:/register";
        }
        return "redirect:/register/confirm";
    }
}
