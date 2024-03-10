package am.itspace.jobboard.security;

import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userService.findByEmail(username) == null) {
            throw new UsernameNotFoundException("User with " + username + " dose not exist!");
        }
        return new SpringUser(userService.findByEmail(username));
    }
}
