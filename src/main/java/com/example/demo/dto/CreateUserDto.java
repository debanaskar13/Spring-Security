package com.example.demo.dto;

import com.example.demo.entities.Role;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateUserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String primaryEmail;
    private String mobileNumber;
    private char gender;
    private boolean isHost;
    private Role role;
    
    
}
