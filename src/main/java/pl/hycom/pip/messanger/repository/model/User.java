package pl.hycom.pip.messanger.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

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
    @Size(min = 2, max = 40)
    @Pattern(regexp = "^\\p{L}{2,40}$")
    private String firstName;

    @NotNull
    @Column(length = 40)
    @Size(min = 2, max = 40)
    @Pattern(regexp = "^\\p{L}{2,40}$")
    private String lastName;

    @NonNull
    @NotNull
    @Column(length = 40, unique = true)
    @Size(min = 6, max = 40)
    @Pattern(regexp = "^[a-z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-z0-9.-]+$")
    private String email;

    @Column(length = 64)
    @Size(min = 5, max = 64)
    private String password;

    @NotNull
    @Column
    @Pattern(regexp = "^(\\+48)[5-9][0-9]{8}$")
    private String phoneNumber;

    @Column
    private String profileImageUrl;

    @Column
    private boolean credentialsNonExpired = true;

    @Column
    private boolean accountNonExpired = true;

    @Column
    private boolean accountNonLocked = true;

    @Column
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

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
