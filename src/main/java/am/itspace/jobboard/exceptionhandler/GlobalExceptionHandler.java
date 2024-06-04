package am.itspace.jobboard.exceptionhandler;

import am.itspace.jobboard.exception.OAuth2EmailOrNameNotExistException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OAuth2EmailOrNameNotExistException.class)
    public String handleOAuth2EmailOrNameNotExistException(OAuth2EmailOrNameNotExistException ex, ModelMap modelMap) {
        String errorMessage = "Your email or name is not provided. Please make sure to grant access to your email and name.";
        modelMap.addAttribute("msg", errorMessage);
        return "login";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handle404NotFoundException() {
        return "/404-page";
    }

    @ExceptionHandler(Exception.class)
    public String handle500ServerException() {
        return "/500-page";
    }
}
