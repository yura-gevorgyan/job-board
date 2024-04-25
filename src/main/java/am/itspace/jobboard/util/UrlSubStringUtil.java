package am.itspace.jobboard.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public abstract class UrlSubStringUtil {
    public static String  removeLastCharacterFromQueryString (HttpServletRequest httpServletRequest) {
        String queryString = httpServletRequest.getQueryString();
        int length = queryString.length() - 1;
        return queryString.substring(0, length);
    }
}
