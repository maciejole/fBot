package pl.hycom.pip.messanger.mail;

import lombok.Data;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

/**
 * Created by Piotr on 31.05.2017.
 */
@Data
public class Message {

    private String from;

    private List<String> to;

    private String subject;

    private String content;

    public Message(String from, List<String> to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

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
