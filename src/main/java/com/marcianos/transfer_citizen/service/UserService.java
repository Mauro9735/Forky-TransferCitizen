package com.marcianos.transfer_citizen.service;

import com.marcianos.transfer_citizen.entity.User;
import com.marcianos.transfer_citizen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
