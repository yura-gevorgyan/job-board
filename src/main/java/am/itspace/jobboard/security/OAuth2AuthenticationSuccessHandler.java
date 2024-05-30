package am.itspace.jobboard.security;

import am.itspace.jobboard.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final SecurityService securityService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        User user = securityService.getCurrentUser();

        if (user != null && user.isActivated() && !user.isDeleted()) {
            getRedirectStrategy().sendRedirect(request, response, "/");
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/login/success/oath2");
        }
    }
}
