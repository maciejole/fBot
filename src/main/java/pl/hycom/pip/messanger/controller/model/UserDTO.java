package pl.hycom.pip.messanger.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Monia on 2017-05-27.
 */
@Data
@NoArgsConstructor
public class UserDTO {
    private Integer id;

    @Size(min = 2, max = 40, message = "{validation.error.firstname.size}")
    @Pattern(regexp = "^\\p{L}{2,40}$")
    private String firstName;

    @Size(min = 2, max = 40, message = "{validation.error.lastname.size}")
    @Pattern(regexp = "^\\p{L}{2,40}$")
    private String lastName;

    @NonNull
    @Size(min = 6, max = 40)
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{validation.error.email.format}")
    private String email;

    @Size(min = 8, max = 64)
    private String password;

    @Pattern(regexp = "^(\\+48)[5-9][0-9]{8}$", message = "{validation.error.phonenumber.format}")
    private String phoneNumber;

    private Set<RoleDTO> roles = new HashSet<>();

    private String profileImageUrl;

}
