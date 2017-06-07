package pl.hycom.pip.messanger.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Created by Piotr on 21.05.2017.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(SimpleMailMessage message) {
        try {
            javaMailSender.send(message);
            log.info("Email sent.");
        } catch (Exception e) {
            log.error("Failed to send email.");
        }
    }

    /*
     * private MimeMessage constructEmail(String to, String subject, String content) {
     * MimeMessage message = javaMailSender.createMimeMessage();
     * try {
     * MimeMessageHelper helper = new MimeMessageHelper(message, true);
     * helper.setTo(to);
     * helper.setFrom("messenger.recommendations2017@gmail.com");
     * helper.setSubject(subject);
     * helper.setText(content, true);
     *
     * } catch (MessagingException e) {
     * log.info(e.getMessage());
     * }
     * return message;
     * }
     */

    /**
     * Created by Piotr on 31.05.2017.
     */
    @Data
    @AllArgsConstructor
    public static class Message {

        private String from;

        private List<String> to;

        private String subject;

        private String content;

        public SimpleMailMessage constructEmail() {
            String[] array = new String[to.size()];
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to.toArray(array));
            message.setSubject(subject);
            message.setText(content);
            return message;
        }

    }
}
