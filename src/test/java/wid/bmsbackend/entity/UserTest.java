package wid.bmsbackend.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    @DisplayName("User 패스워드 테스트")
    public void testUserPassword() {
        User user = new User();
        user.setPassword("password");
        assertEquals("password", user.getPassword());

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String encodedPassword = encoder.encode("password");
        user.setPassword(encodedPassword);
        System.out.println("encodedPassword = " + encodedPassword);
        assertThat(encoder.matches("password", user.getPassword())).isTrue();


    }

}