package com.example.Shop;

import com.example.Shop.dto.UserRequestDTO;
import com.example.Shop.dto.UserResponseDTO;
import com.example.Shop.dto.UserUpdateDTO;
import com.example.Shop.entity.User;
import com.example.Shop.exceptions.EmailAlreadyExistsException;
import com.example.Shop.exceptions.ResourceNotFoundException;
import com.example.Shop.repository.UserRepository;
import com.example.Shop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UNIT TEST for UserService
 *
 * PURPOSE:
 * Test business logic without starting Spring Boot
 * and without using a real database.
 *
 * Repository layer is mocked using Mockito.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    /**
     * Fake repository created by Mockito.
     * Simulates database behaviour.
     */
    @Mock
    private UserRepository repository;

    /**
     * Service under test.
     * Mockito injects mocked repository automatically.
     */
    @InjectMocks
    private UserService userService;

    // reusable test objects
    private User user;
    private UserRequestDTO request;

    /**
     * Executed before each test.
     * Creates common test data.
     */
    @BeforeEach
    void setup(){

        // Entity representing DB object
        user = new User(
                1L,
                "marcel",
                "marcel@gmail.com",
                null
        );

        // DTO representing API input
        request = new UserRequestDTO(
                "marcel",
                "marcel@gmail.com"
        );
    }

    // ================= CREATE USER =================

    /**
     * Scenario:
     * Email does NOT exist -> user should be saved.
     *
     * FLOW:
     * DTO -> Service -> Entity -> Repository -> DTO response
     */
    @Test
    void createUser_ShouldSaveUser(){

        // GIVEN (mock setup)
        when(repository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        when(repository.save(any(User.class)))
                .thenReturn(user);

        // WHEN (execute business logic)
        UserResponseDTO response = userService.createUser(request);

        // THEN (verify result)
        assertEquals("marcel", response.name());

        // ensure save was called
        verify(repository).save(any(User.class));
    }

    /**
     * Scenario:
     * Email already exists.
     *
     * EXPECTED:
     * - Exception thrown
     * - save() never called
     */
    @Test
    void createUser_ShouldTrowException_WhenEmailExists(){

        when(repository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.createUser(request)
        );

        verify(repository, never()).save(any());
    }

    // ================= GET BY ID =================

    /**
     * Scenario:
     * User exists in database.
     *
     * Tests entity -> DTO mapping.
     */
    @Test
    void getByid_ShouldReturnUser(){

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponseDTO responseDTO = userService.getById(1L);

        assertEquals("marcel", responseDTO.name());
        assertEquals("marcel@gmail.com", responseDTO.email());
    }

    /**
     * Scenario:
     * User missing.
     *
     * EXPECTED:
     * ResourceNotFoundException thrown.
     */
    @Test
    void getById_ShouldThrowException_WhenMissing(){

        when(repository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getById(2L)
        );
    }

    // ================= GET ALL =================

    /**
     * Tests retrieving all users.
     *
     * Verifies:
     * - repository call
     * - mapping list<Entity> -> list<DTO>
     */
    @Test
    void getAll_ShouldReturnUsers(){

        when(repository.findAll())
                .thenReturn(List.of(user));

        List<UserResponseDTO> users = userService.getAll();

        assertEquals(1, users.size());
        assertEquals("marcel", users.getFirst().name());

        verify(repository).findAll();
    }

    // ================= UPDATE =================

    /**
     * Tests update flow.
     *
     * PROCESS:
     * 1. Load existing user
     * 2. Modify fields
     * 3. Save updated entity
     * 4. Return mapped DTO
     */
    @Test
    void update_ShouldModifyUser(){

        UserUpdateDTO updateDTO =
                new UserUpdateDTO("updated", "updated@gmail.com");

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        when(repository.save(user))
                .thenReturn(user);

        UserResponseDTO responseDTO =
                userService.update(1L, updateDTO);

        assertEquals("updated", responseDTO.name());
        assertEquals("updated@gmail.com", responseDTO.email());

        verify(repository).save(user);
    }

    // ================= DELETE =================

    /**
     * Tests delete operation.
     *
     * Only repository interaction is verified
     * because method returns void.
     */
    @Test
    void delete_ShouldRemoveUser(){

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        doNothing().when(repository).delete(user);

        userService.delete(1L);

        verify(repository).delete(user);
    }
}