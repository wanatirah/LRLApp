package com.example.LRLApp.Service;

import com.example.LRLApp.Model.Users;
import com.example.LRLApp.Web.Dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    Users save(UserRegistrationDto registrationDto);
}
