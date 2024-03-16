package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.User;

public interface SendMailService {

    void send(String to, String subject, String message);

    void sendEmailConfirmMail(User user);

    void sendEmailResumeDeleted(User user);

    void sendEmailJobDeleted(User user);

    void sendEmailJobRecovered(User user);

    void sendEmailCompanyDeleted(User user);

    void sendEmailAccountBlocked(User user);

    void sendEmailAccountUnlocked(User user);
}
