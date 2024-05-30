package am.itspace.jobboard.exception.exceptionHandler;

import am.itspace.jobboard.exception.OAuth2EmailOrNameNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OAuth2EmailOrNameNotExistException.class)
    public String handleOAuth2EmailOrNameNotExistException(OAuth2EmailOrNameNotExistException ex, ModelMap modelMap) {
        String errorMessage = "Your email or name is not provided. Please make sure to grant access to your email and name.";
        modelMap.addAttribute("msg", errorMessage);
        return "login";
    }
}
