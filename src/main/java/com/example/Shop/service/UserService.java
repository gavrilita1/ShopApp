package com.example.Shop.service;

import com.example.Shop.dto.UserRequestDTO;
import com.example.Shop.dto.UserResponseDTO;
import com.example.Shop.dto.UserUpdateDTO;
import com.example.Shop.entity.User;
import com.example.Shop.exceptions.EmailAlreadyExistsException;
import com.example.Shop.exceptions.ResourceNotFoundException;
import com.example.Shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDTO createUser(UserRequestDTO request){
        repository.findByEmail( request.email() )
                .ifPresent(u -> { throw  new EmailAlreadyExistsException(u.getEmail()); });

        User user = new User(null, request.name(), request.email(), null);
        User savedUser =  repository.save(user);

        return toDto(savedUser);
    }

    public UserResponseDTO getById(Long id){
        return toDto(repository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("user not found")));
    }

    public List<UserResponseDTO> getAll(){
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public UserResponseDTO update(Long id, UserUpdateDTO dto){
        User user =  repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("user not found"));
        user.setUsername(dto.name());
        user.setEmail(dto.email());

        return toDto(repository.save(user));
    }

    public void delete(Long id){
        User user =  repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("user not found"));
        repository.delete(user);
    }

    private UserResponseDTO toDto(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

}
