package pl.hycom.pip.messanger.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Piotr on 03.06.2017.
 */
@Data
@NoArgsConstructor
public class ResetPassword {

    @NotNull
    @NotEmpty
    private String resetToken;

    @NotNull
    @Size(min = 6, max = 40, message = "{validation.error.email.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{validation.error.email.format}")
    private String userMail;

    @Size(min = 6, max = 64, message = "{validation.error.password.size}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$", message = "{validation.error.password.format}")
    private String newPassword;
}
