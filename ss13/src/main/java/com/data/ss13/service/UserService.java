package com.data.ss13.service;

import com.data.ss13.model.entity.User;
import com.data.ss13.model.dto.request.UserLogin;
import com.data.ss13.model.dto.request.UserRegister;
import com.data.ss13.model.dto.response.JWTResponse;

public interface UserService{
    User registerUser(UserRegister userRegister);

    JWTResponse login(UserLogin userLogin);

    User editUser(User user);

    User getCurrentUser();
}
