package am.itspace.jobboard.scheduler;

import am.itspace.jobboard.entity.User;
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

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void cleanUnconfirmedUsers() {
        List<User> userByActivated = userService.findUserByActivated(false);
        for (User user : userByActivated) {
            if (user.getRegisterDate() != null && user.getRegisterDate().before(new Date(System.currentTimeMillis() - 60000))) {
                userService.delete(user);
                sendMailService.send(user.getEmail(), "Deleting register dates", "Your dates is deleted please register again");
            }
        }
    }
}
