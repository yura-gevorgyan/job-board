package am.itspace.jobboard.util;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class AddErrorMessageUtil {

    public static void addMessageToModel(String msg, ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
    }

    public static void addErrorMessage(RedirectAttributes redirectAttributes, BindingResult bindingResult) {
        StringBuilder errorMsgBuilder = new StringBuilder("Error: ");
        bindingResult.getFieldErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            if (errorMessage != null){
                errorMsgBuilder.append(" ").append(errorMessage);
            }
        });
        redirectAttributes.addFlashAttribute("msg", errorMsgBuilder.toString());
    }
}
