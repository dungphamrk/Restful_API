package ra.ss14.service;

import ra.ss14.model.dto.request.UserRegister;
import ra.ss14.model.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO registerUser(UserRegister userRegister);
    UserResponseDTO getCurrentUser();
    boolean isUsernameTaken(String username);
    boolean isEmailTaken(String email);
}
