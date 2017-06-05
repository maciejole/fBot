package pl.hycom.pip.messanger.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.hycom.pip.messanger.mail.EmailSender;

import javax.inject.Inject;

/**
 * Created by Piotr on 21.05.2017.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class EmailService implements EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(SimpleMailMessage message) {
        try {
            javaMailSender.send(message);
            log.info("Email sent.");
        } catch (Exception e) {
            log.error("Failed to send email.");
        }
    }

    /*private MimeMessage constructEmail(String to, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom("messenger.recommendations2017@gmail.com");
            helper.setSubject(subject);
            helper.setText(content, true);

        } catch (MessagingException e) {
            log.info(e.getMessage());
        }
        return message;
    }*/

}
