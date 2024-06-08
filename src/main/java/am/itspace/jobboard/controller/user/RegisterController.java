package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.exception.EmailIsPresentException;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import static am.itspace.jobboard.util.AddErrorMessageUtil.addErrorMessage;

@Slf4j
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
        if (confirmEmailCode == null || confirmEmailCode.isBlank()) {
            return "redirect:/";
        }
        if (userService.confirmEmail(confirmEmailCode) != null) {
            log.info("User has been confirmed with the confirm code of {}", confirmEmailCode);
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("msg", "Invalid confirm code.");
        return "redirect:/register/confirm";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute User user,
                           @RequestParam String confirmPassword,
                           RedirectAttributes redirectAttributes,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError("password").getDefaultMessage();
            redirectAttributes.addFlashAttribute("msg", errorMessage);
            return "redirect:/register";
        }

        if (user.getRole() == null || user.getRole().toString().isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Choose your type.");
            return "redirect:/register";
        }

        try {
            userService.register(user, confirmPassword, user.getRole());
            log.info("User by {} id and {} username, has registered with the Role of {}", user.getId(), user.getEmail(), user.getRole().getName());

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
