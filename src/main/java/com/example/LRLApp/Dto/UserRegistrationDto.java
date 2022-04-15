package com.example.LRLApp.Dto;

import lombok.*;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegistrationDto { // request user's input for registration
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserRegistrationDto() {

    }
}
