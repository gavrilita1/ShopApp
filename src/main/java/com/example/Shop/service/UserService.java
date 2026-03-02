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

    // Repository handling persistence layer
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * CREATE USER
     *
     * INPUT:
     *  UserRequestDTO coming from API request
     *
     * PROCESS:
     *  1. Check if email already exists (business validation)
     *  2. Convert DTO -> Entity
     *  3. Save entity
     *  4. Convert Entity -> Response DTO
     *
     * OUTPUT:
     *  UserResponseDTO
     */
    public UserResponseDTO createUser(UserRequestDTO request){

        // Business rule: email must be unique
        repository.findByEmail(request.email())
                .ifPresent(u -> {
                    throw new EmailAlreadyExistsException(u.getEmail());
                });

        // DTO -> Entity mapping
        User user = new User(
                null,
                request.name(),
                request.email(),
                null
        );

        User savedUser = repository.save(user);

        // Entity -> DTO mapping
        return toDto(savedUser);
    }

    /**
     * READ USER BY ID
     */
    public UserResponseDTO getById(Long id){
        return toDto(
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("user not found"))
        );
    }

    /**
     * READ ALL USERS
     *
     * Demonstrates stream mapping.
     */
    public List<UserResponseDTO> getAll(){
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * UPDATE USER
     *
     * PROCESS:
     *  1. Fetch user
     *  2. Modify fields
     *  3. Save entity
     *  4. Return mapped DTO
     */
    public UserResponseDTO update(Long id, UserUpdateDTO dto){

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("user not found"));

        user.setUsername(dto.name());
        user.setEmail(dto.email());

        return toDto(repository.save(user));
    }

    /**
     * DELETE USER
     *
     * Ensures user exists before deletion.
     */
    public void delete(Long id){

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("user not found"));

        repository.delete(user);
    }

    /**
     * ENTITY -> DTO mapping method.
     *
     * Keeping mapping centralized:
     *  ✔ avoids duplicated code
     *  ✔ easier maintenance
     */
    private UserResponseDTO toDto(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
