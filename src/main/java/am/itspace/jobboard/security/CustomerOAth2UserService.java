package am.itspace.jobboard.security;

import am.itspace.jobboard.config.PasswordProperties;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomerOAth2UserService extends DefaultOAuth2UserService {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String gitHubClientId;

    private final UserService userService;
    private final PasswordProperties passwordProperties;
    private final FetchUserFromGitHub fetchUserFromGitHub;
    private final HttpSession httpSession;

    @SneakyThrows
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oauth2User = super.loadUser(userRequest);
        String requestClientId = userRequest.getClientRegistration().getClientId();

        String email;
        String name;

        if (requestClientId.equals(gitHubClientId)) {
            email = fetchUserFromGitHub.fetchEmailFromGitHub(userRequest.getAccessToken().getTokenValue());
            name = fetchUserFromGitHub.fetchUserNameFromGitHub(userRequest.getAccessToken().getTokenValue());
            httpSession.setAttribute("gitHubUserEmail", email);
        } else {
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
        }

        User existingUser = userService.findByEmail(email);
        if (existingUser == null) {

            String[] nameParts = name.split("[ -]");
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String surname = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "";

            new User();
            existingUser = User.builder()
                    .name(firstName)
                    .surname(surname)
                    .email(email)
                    .password(passwordProperties.getUserPassword())
                    .role(Role.JOB_SEEKER)
                    .registerDate(new Date())
                    .activated(true)
                    .isDeleted(false)
                    .build();
            existingUser = userService.save(existingUser);
        }
        new SpringUser(existingUser);
        return oauth2User;
    }
}
