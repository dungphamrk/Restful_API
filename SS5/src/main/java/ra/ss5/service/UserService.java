package ra.ss5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.ss5.entity.User;
import ra.ss5.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new RuntimeException("Không tìm thấy người dùng với ID: " + id);
    }
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Tên người dùng đã tồn tại: " + user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
        }

        return userRepository.save(user);
    }
    public User updateUser(Long id, User user) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (!existingUser.getUsername().equals(user.getUsername()) &&
                    userRepository.existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Tên người dùng đã tồn tại: " + user.getUsername());
            }

            if (!existingUser.getEmail().equals(user.getEmail()) &&
                    userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
            }

            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());

            return userRepository.save(existingUser);
        }
        throw new RuntimeException("Không tìm thấy người dùng với ID: " + id);
    }
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + id);
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với username: " + username));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));
    }
}


