package com.example.Shop.service;

import com.example.Shop.dto.UserDTO;
import com.example.Shop.entity.User;
import com.example.Shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    public UserDTO save(UserDTO dto) {
        User user = new User(null, dto.username(), dto.email(), null);
        User saved = repository.save(user);
        return new UserDTO(saved.getId(), saved.getUsername(), saved.getEmail());
    }
    public List<UserDTO> findAll() {
        return repository.findAll().stream()
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail())).toList();
    }
}