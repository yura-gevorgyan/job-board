package am.itspace.jobboard.security;

import am.itspace.jobboard.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;

@Getter
public class SpringUser extends org.springframework.security.core.userdetails.User {

    private final User user;


    public SpringUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isActivated(), !user.isDeleted(), true, true, AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }
}
