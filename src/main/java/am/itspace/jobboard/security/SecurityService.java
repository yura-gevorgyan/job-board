package am.itspace.jobboard.security;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final HttpSession httpSession;

    public User getCurrentUser() {

        String email;

        try {

            String gitHubUserEmail = (String) httpSession.getAttribute("gitHubUserEmail");
            if (gitHubUserEmail != null) {
                email = gitHubUserEmail;

            } else {
                OAuth2User defaultOAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                email = defaultOAuth2User.getAttribute("email");
            }

        } catch (Exception e) {

            try {
                SpringUser springUser = (SpringUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                email = springUser.getUser().getEmail();

            } catch (Exception ex) {
                return null;
            }
        }
        return userService.findByEmail(email);
    }
}
