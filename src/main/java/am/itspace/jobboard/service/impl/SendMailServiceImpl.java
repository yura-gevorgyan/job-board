package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.service.SendMailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void send(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    @Async
    public void sendEmailConfirmMail(User user) {

        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("message/SendGeneratedTokenMessage.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Confirm code");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendEmailResumeDeleted(User user) {

        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("message/SendInfo-ResumeDeleted.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Notification Regarding Deletion of Your Resume");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendEmailJobDeleted(User user) {
        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("message/SendInfo-JobDeleted.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Notification Regarding Deletion of Your Job Advertisement");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendEmailJobRecovered(User user) {
        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("message/SendInfo-JobRecovered.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Notification Regarding Recovery of Your Job Advertisement");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendEmailCompanyDeleted(User user) {
        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("message/SendInfo-CompanyDeleted.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Notification Regarding Deletion of Your Company");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendEmailAccountBlocked(User user) {
        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("message/SendInfo-AccountBlocked.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Account Blocked");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendEmailAccountUnlocked(User user) {
        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("message/SendInfo-AccountUnlocked.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Account Unlocked");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailConfirmMailForDelete(User user) {
        final Context ctx = new Context();
        ctx.setVariable("user", user);

        final String htmlContent = templateEngine.process("message/SendGeneratedTokenMessageForDelete.html", ctx);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Confirm code");
            message.setTo(user.getEmail());

            message.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

