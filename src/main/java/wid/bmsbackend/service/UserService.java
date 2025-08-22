package wid.bmsbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wid.bmsbackend.dto.UserRequest;
import wid.bmsbackend.dto.UserResponse;
import wid.bmsbackend.entity.Role;
import wid.bmsbackend.entity.User;
import wid.bmsbackend.repository.RoleRepository;
import wid.bmsbackend.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::of);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void addUser(UserRequest userRequest) {
        Role admin = roleRepository.findByName("ROLE_ADMIN").orElseThrow(EntityNotFoundException::new);
        String password = passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(password);
        User user = userRequest.toEntity();
        user.setRoles(Set.of(admin));
        userRepository.save(user);
    }

    public void updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        if (userRequest.getPassword() == null) {
            userRequest.setPassword(user.getPassword());
            user.update(userRequest);
            userRepository.save(user);
        }
        else {
            String password = passwordEncoder.encode(userRequest.getPassword());
            user.setPassword(password);
            userRepository.save(user);
        }

    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
