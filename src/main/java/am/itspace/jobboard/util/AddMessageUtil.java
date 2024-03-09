package am.itspace.jobboard.util;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class AddMessageUtil {
    public static void addMessageToModel(String msg, ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
    }
}
