package pl.hycom.pip.messanger.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Monia on 2017-05-20.
 */
@Data
@Entity
@Table(name = "USERS")
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(length = 40)
    @Size(min = 3, max = 40)
    @Pattern(regexp = "^[a-zA-ZĘÓĄŚŁŹŻŃĆęóąśźżćńł]{3,40}$")
    private String firstName;

    @NotNull
    @Column(length = 40)
    @Size(min = 3, max = 40)
    @Pattern(regexp = "^[a-zA-ZĘÓĄŚŁŹŻŃĆęóąśźżćńł]{3,40}$")
    private String lastName;

    @NotNull
    @Column(length = 40, unique = true)
    @Size(min = 6, max = 40)
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Column(length = 64)
    @Size(min = 5, max = 64)
    private String password;

    @Column
    private boolean credentialsNonExpired = true;

    @Column
    private boolean accountNonExpired = true;

    @Column
    private boolean accountNonLocked = true;

    @Column
    private boolean enabled = true;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
    @NotNull
    @Column
    @Pattern(regexp = "^(\\+48)[5-9][0-9]{8}$")
    private String phoneNumber;
    @Column
    private String profileImageUrl;

    public User(String firstName, String lastName, String email, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
