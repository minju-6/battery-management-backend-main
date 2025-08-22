package wid.bmsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wid.bmsbackend.entity.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String nickname;
    private String username;
    private String password;
    private String phone;

    public User toEntity(){
        return User.builder()
                .nickname(nickname)
                .username(username)
                .password(password)
                .phone(phone)
                .build();
    }
}
