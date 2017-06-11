package pl.hycom.pip.messanger.repository.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

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
