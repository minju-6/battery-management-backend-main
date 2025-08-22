package wid.bmsbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wid.bmsbackend.entity.Role;
import wid.bmsbackend.entity.User;
import wid.bmsbackend.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {
    UserRepository userRepository;
    CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    @DisplayName("User 가 존재하지 않는 경우 예외가 발생한다.")
    void loadUserByUsername_whenUserDoesNotExist_throwsException() {
        String username = "testUser";
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername(username));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("User 가 존재하는 경우 UserDetailsService 가 UserDetails를 반환한다.")
    void loadUserByUsername_whenUserExists_returnUserDetails() {
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(createUser()));

        customUserDetailsService.loadUserByUsername(username);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("User 가 존재하고 RoleSet 가 empty 일때도 UserDetails를 반환한다.")
    void loadUserByUsername_whenUserExistsAndRoleSetIsEmpty_returnUserDetails() {
        String username = "testUser";
        User user = createUser();
        user.setRoles(Set.of());
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        customUserDetailsService.loadUserByUsername(username);
        verify(userRepository, times(1)).findByUsername(username);
    }

    User createUser() {
        return User.builder().id(1L).username("testUser").password("testPassword")
                .roles(createRole())
                .accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false).
                build();
    }

    Set<Role> createRole() {
        return Set.of(Role.builder().id(1L).name("ROLE_USER").users(null).build());
    }


}