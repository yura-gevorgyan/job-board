package am.itspace.jobboard.security;

import am.itspace.jobboard.config.PasswordProperties;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

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

            existingUser = User.builder()
                    .name(firstName)
                    .surname(surname)
                    .email(email)
                    .password(passwordProperties.getOAuth2UserPassword())
                    .role(Role.JOB_SEEKER)
                    .registerDate(new Date())
                    .activated(false)
                    .isDeleted(false)
                    .build();
            userService.save(existingUser);
        }

        OAuth2User customOAuth2User = getoAuth2User(existingUser, requestClientId, oauth2User);


        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities())
        );

        return customOAuth2User;
    }

    private OAuth2User getoAuth2User(User existingUser, String requestClientId, OAuth2User oauth2User) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(existingUser.getRole().name());

        OAuth2User customOAuth2User;
        if (requestClientId.equals(gitHubClientId)) {
            customOAuth2User = new DefaultOAuth2User(
                    Collections.singletonList(authority),
                    oauth2User.getAttributes(),
                    "login"
            );
        } else {
            customOAuth2User = new DefaultOAuth2User(
                    Collections.singletonList(authority),
                    oauth2User.getAttributes(),
                    "name"
            );
        }
        return customOAuth2User;
    }
}

@Getter
class CustomOAuth2User extends DefaultOAuth2User {
    private final String email;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String email) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return super.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public String getName() {
        return super.getName();
    }
}
