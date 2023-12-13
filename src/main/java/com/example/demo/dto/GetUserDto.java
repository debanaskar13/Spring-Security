package com.example.demo.dto;

import java.util.Date;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GetUserDto {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String primaryEmail;
    private String secondaryEmail;
    private String mobileNumber;
    private boolean isActive;
    private char gender;
    private String intro;
    private String profilePhoto;
    private boolean isHost;
    private Date registeredAt;
    private Date lastLogin;
}
