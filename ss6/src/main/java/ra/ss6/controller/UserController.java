package ra.ss6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ra.ss6.model.DataResponse;
import ra.ss6.model.User;
import ra.ss6.service.UserService;

import java.util.List;

@Controller
@RequestMapping("users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<DataResponse<List<User>>> getAllUser()  {
        return new ResponseEntity<>(new DataResponse<List<User>>(userService.userRepository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse<User>> addUser(@RequestBody User user)  {
        return new ResponseEntity<>(new DataResponse<>(userService.userRepository.save(user), HttpStatus.OK), HttpStatus.OK);
    }
    @GetMapping("{id}")
    public  ResponseEntity<DataResponse<User>> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(new DataResponse<>(userService.getUserById(id), HttpStatus.OK), HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return new ResponseEntity<>(new DataResponse<>(userService.deleteUser(id),HttpStatus.NO_CONTENT),HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<DataResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        return new ResponseEntity<>(new DataResponse<>(userService.updateUser(user,id), HttpStatus.OK), HttpStatus.OK);
    }

}
