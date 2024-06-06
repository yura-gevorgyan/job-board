package am.itspace.jobboard.scheduler;

import am.itspace.jobboard.config.PasswordProperties;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Slf4j
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
                        !user.getPassword().equals(passwordProperties.getOAuth2UserPassword()))
                .forEach(user -> {
                    userService.delete(user);
                    sendMailService.send(user.getEmail(),
                            "Account Deletion Notice",
                            "Your account has been deleted because it was not confirmed. Please register again.");
                });
        log.info("Cleaning unconfirmed users has just finished successfully");
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void activeOAuth2Users() {

        List<User> unactivatedOAuth2Users = userService.findUserByPasswordAndIsActivatedFalse(passwordProperties.getOAuth2UserPassword());
        Date oneMinuteAgo = new Date(System.currentTimeMillis() - 60000);

        unactivatedOAuth2Users.stream()
                .filter(user -> user.getRegisterDate() != null &&
                        user.getRegisterDate().before(oneMinuteAgo))
                .forEach(user -> {
                    user.setActivated(true);
                    userService.save(user);
                    sendMailService.send(user.getEmail(),
                            "Account Activation Notice",
                            "Your account has activated and your type is finally Job Seeker because you don't select yor type after login with social media.");
                });
        log.info("Auto activation of non activated users has just finished successfully");
    }
}
