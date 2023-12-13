package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String secondaryEmail;
    private String mobileNumber;
    private boolean isActive;
    private String intro;
    private String profilePhoto;
    private boolean isHost;

}
