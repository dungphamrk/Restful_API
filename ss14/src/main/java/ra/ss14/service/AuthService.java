package ra.ss14.service;

import ra.ss14.model.dto.request.UserLogin;
import ra.ss14.model.dto.response.JWTResponse;

public interface AuthService {
    JWTResponse login(UserLogin userLogin);
}