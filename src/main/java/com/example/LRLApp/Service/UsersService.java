package com.example.LRLApp.Service;

import com.example.LRLApp.Model.Users;
import com.example.LRLApp.Dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {

    Users save(UserRegistrationDto registrationDto);

    List<Users> getAll();
}
