package am.itspace.jobboard.security;

import am.itspace.jobboard.config.PasswordProperties;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.exception.OAuth2EmailOrNameNotExistException;
import am.itspace.jobboard.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomerOAth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final PasswordProperties passwordProperties;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (StringUtils.isBlank(email) || StringUtils.isBlank(name)) {
            throw new OAuth2EmailOrNameNotExistException();
        }

        User existingUser = userService.findByEmail(email);
        if (existingUser == null) {

            String[] nameParts = name.split(" ");
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String surname = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "";

            existingUser = new User();
            existingUser.setName(firstName);
            existingUser.setSurname(surname);
            existingUser.setEmail(email);
            existingUser.setPassword(passwordProperties.getUserPassword());
            existingUser.setRole(Role.JOB_SEEKER);
            existingUser.setRegisterDate(new Date());
            existingUser.setActivated(false);
            existingUser.setDeleted(false);
            existingUser = userService.save(existingUser);

        }
        new SpringUser(existingUser);
        return oauth2User;
    }
}
