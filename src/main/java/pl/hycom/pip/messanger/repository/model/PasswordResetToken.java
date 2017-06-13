package pl.hycom.pip.messanger.repository.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Created by Piotr on 27.05.2017.
 */
@Data
@Entity
@Table(name = "PasswordResetToken")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private LocalDateTime expiryDate;

}
