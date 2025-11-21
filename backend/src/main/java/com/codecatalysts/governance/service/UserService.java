package com.codecatalysts.governance.service;

import com.codecatalysts.governance.entity.User;
import com.codecatalysts.governance.entity.UserRole;
import com.codecatalysts.governance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    public List<User> getCitizens() {
        return userRepository.findByRole(UserRole.CITIZEN);
    }

    public List<User> getOfficials() {
        return userRepository.findByRole(UserRole.OFFICIAL);
    }

    public List<User> getAdmins() {
        return userRepository.findByRole(UserRole.ADMIN);
    }
}

