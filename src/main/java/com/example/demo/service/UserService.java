package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CreateUserDto;
import com.example.demo.dto.GetUserDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.entities.EmailDetails;
import com.example.demo.entities.User;

public interface UserService {

    GetUserDto createUser(CreateUserDto user);

    GetUserDto getSingleUser(int userId) throws Exception;

    void deleteUser(int userId) throws Exception;

    List<GetUserDto> getAllUser();

    GetUserDto updateUser(int userId,UpdateUserDto userDto) throws Exception;

    void updateResetPasswordToken(String token,String email);

    User getUserByResetPasswordToken(String token);

    void updatePassword(User user,String newPassword);

    void sentEmail(EmailDetails emailDetails, String resetPasswordLink);

}
