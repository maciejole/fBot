package pl.hycom.pip.messanger.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Monia on 2017-05-27.
 */
@Data
@NoArgsConstructor
public class UserDTO {
    private Integer id;

    @Size(min = 3, max = 40)
    @Pattern(regexp = "^[a-zA-ZĘÓĄŚŁŹŻŃĆęóąśźżćńł]{3,40}$")
    private String firstName;

    @Size(min = 3, max = 40)
    @Pattern(regexp = "^[a-zA-ZĘÓĄŚŁŹŻŃĆęóąśźżćńł]{3,40}$")
    private String lastName;

    @Size(min = 6, max = 40)
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Size(min = 8, max = 64)
    private String password;

    @Pattern(regexp = "^(\\+48)[5-9][0-9]{8}$")
    private String phoneNumber;

    private Set<RoleDTO> roles = new HashSet<>();

    private String profileImageUrl;
}
