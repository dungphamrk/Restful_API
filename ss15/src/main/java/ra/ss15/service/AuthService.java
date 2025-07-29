package ra.ss15.service;

import ra.ss15.model.dto.request.LoginRequest;
import ra.ss15.model.dto.request.RegisterRequest;
import ra.ss15.model.dto.response.JWTResponse;

public interface AuthService{
    void register(RegisterRequest req);
    JWTResponse login(LoginRequest req);
}