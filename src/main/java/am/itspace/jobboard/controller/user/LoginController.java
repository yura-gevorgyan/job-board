package am.itspace.jobboard.controller.user;

import am.itspace.jobboard.util.AddErrorMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

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

}
