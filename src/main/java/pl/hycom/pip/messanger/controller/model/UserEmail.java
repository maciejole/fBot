package pl.hycom.pip.messanger.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Piotr on 04.06.2017.
 */
@Data
@NoArgsConstructor
public class UserEmail {

    @NotNull
    @Size(min = 6, max = 40, message = "{validation.error.email.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{validation.error.email.format}")
    private String userMail;
}
