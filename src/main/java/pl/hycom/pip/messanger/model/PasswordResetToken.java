package pl.hycom.pip.messanger.model;

import lombok.Data;
import pl.hycom.pip.messanger.repository.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Piotr on 27.05.2017.
 */
@Data
@Entity
@Table(name = "PasswordResetToken")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;

    private LocalDateTime expiryDate;

}
