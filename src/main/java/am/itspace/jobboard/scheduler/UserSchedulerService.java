package am.itspace.jobboard.scheduler;

import am.itspace.jobboard.config.PasswordProperties;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSchedulerService {

    private final UserService userService;
    private final SendMailService sendMailService;
    private final PasswordProperties passwordProperties;

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void cleanUnconfirmedUsers() {

        List<User> unconfirmedUsers = userService.findUserByActivated(false);
        Date oneMinuteAgo = new Date(System.currentTimeMillis() - 60000);

        unconfirmedUsers.stream()
                .filter(user -> user.getRegisterDate() != null &&
                        user.getRegisterDate().before(oneMinuteAgo) &&
                        !user.getPassword().equals(passwordProperties.getUserPassword()))

                .forEach(user -> {
                    userService.delete(user);
                    sendMailService.send(user.getEmail(), "Account Deletion Notice", "Your account has been deleted because it was not confirmed. Please register again.");
                });
    }
}
