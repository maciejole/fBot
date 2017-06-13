package pl.hycom.pip.messanger.repository.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private Integer user;

    private LocalDateTime expiryDate;

}
