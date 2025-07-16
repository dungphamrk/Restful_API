package ra.ss6.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.ss6.model.User;
import ra.ss6.repository.BookRepository;
import ra.ss6.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
   public User createUser(User newUser){
        return userRepository.save(newUser);
   }
   public User updateUser(User updatedUser,Long id){
        userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        return userRepository.save(updatedUser);
   }
   public boolean deleteUser(Long id){
        userRepository.deleteById(id);
        return true;
   }
   
}
