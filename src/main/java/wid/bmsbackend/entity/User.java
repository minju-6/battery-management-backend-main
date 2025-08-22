package wid.bmsbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import wid.bmsbackend.dto.UserRequest;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String nickname;
    private String password;
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    @Builder.Default
    private boolean accountExpired = false;
    @Builder.Default
    private boolean accountLocked = false;
    @Builder.Default
    private boolean credentialsExpired = false;
    @Builder.Default
    private boolean disabled = false;

    public void update(UserRequest userRequest){
        this.username = userRequest.getUsername();
        this.nickname = userRequest.getNickname();
        this.password = getPassword();
        this.phone = userRequest.getPhone();
    }
}
