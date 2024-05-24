package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.exception.NotFoundException;
import am.itspace.jobboard.exception.PasswordNotMuchException;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.AddErrorMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @GetMapping
    public String loginPage(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
        if (error != null && error.equals("true")) {
            AddErrorMessageUtil.addMessageToModel("Invalid Email or Password, Try Again", modelMap);
        }
        return "login";
    }

    @GetMapping("/success")
    public String loginSuccess() {
        return "redirect:/";
    }

    @GetMapping("/success/oath2")
    public String successPage(ModelMap modelMap) {
        Set<Role> nonAdminRoles = EnumSet.complementOf(EnumSet.of(Role.ADMIN));
        modelMap.addAttribute("roles", new ArrayList<>(nonAdminRoles));
        return "OAuth2-success";
    }

    @PostMapping("/success/oath2")
    public String successPage(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

        if (user.getRole() == null || user.getRole().toString().isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Choose your type.");
            return "redirect:/register";
        }

        try {
            userService.updateOAuth2User(user);

        } catch (PasswordNotMuchException e) {
            redirectAttributes.addFlashAttribute("msg", "Passwords do not match.");
            return "redirect:/login/success/oath2";

        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("msg", "User not found. Please check your email and try again.");
            return "redirect:/login/success/oath2";
        }
        return "redirect:/login/success";
    }
}
