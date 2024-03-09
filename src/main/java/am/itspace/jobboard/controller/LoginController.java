package am.itspace.jobboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @GetMapping("/success")
    public String loginSuccess() {
        return "redirect:/";
    }

}
