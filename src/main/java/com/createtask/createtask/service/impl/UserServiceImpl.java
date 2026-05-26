package com.createtask.createtask.service.impl;

import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.exception.DuplicateUserException;
import com.createtask.createtask.exception.UserNotFoundException;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.UserService;
import org.springframework.stereotype.Service;
import com.createtask.createtask.exception.UserRoleMappingExistsException;
import com.createtask.createtask.repository.UserRolesRepository;
import java.util.List;
import java.util.TreeSet;

// Service implementation class for user-related business logic
@Service
public class UserServiceImpl implements UserService {

    // Repository dependency for database operations
    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;

    // Constructor injection for UserRepository and UserRolesRepository
    public UserServiceImpl(UserRepository userRepository,
                           UserRolesRepository userRolesRepository) {

        this.userRepository = userRepository;
        this.userRolesRepository = userRolesRepository;
    }

    // Creates a new user after validation
    @Override
    public AppUser createUser(AppUser user) {

        // Checks if user ID already exists
        if (userRepository.existsById(user.getUserID())) {
            throw new DuplicateUserException(
                    "userID",
                    String.valueOf(user.getUserID())
            );
        }

        // Checks if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateUserException("email", user.getEmail()); // Throws duplicate email exception
        }

        // Checks username contains only letters and spaces
        if (!user.getUsername().matches("^[A-Za-z ]+$")) {

            throw new IllegalArgumentException(
                    "Username must contain only letters"
            );
        }

// Checks full name contains only letters and spaces
        if (!user.getFullName().matches("^[A-Za-z ]+$")) {

            throw new IllegalArgumentException(
                    "FullName must contain only letters"
            );
        }

        // Saves user into database
        return userRepository.save(user);
    }

    // Retrieves user by user ID
    @Override
    public AppUser getUserById(Integer userId) {

        // Finds user by ID or throws exception if absent
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // Retrieves all users from database
    @Override
    public List<AppUser> getAllUsers() {

        // Returns all users as list
        return userRepository.findAll();
    }

    // Retrieves all users sorted using TreeSet
    @Override
    public TreeSet<AppUser> getAllUsersSorted() {

        // Converts list into sorted TreeSet
        return new TreeSet<>(userRepository.findAll());
    }

    // Updates existing user details
    @Override
    public AppUser updateUser(Integer userId, AppUser updatedUser) {

        // Retrieves existing user or throws exception
        AppUser existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Checks duplicate email only if email changed
        if (!existing.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) {

            throw new DuplicateUserException("email", updatedUser.getEmail()); // Throws duplicate email exception
        }

        // Checks username contains only letters and spaces
        if (!updatedUser.getUsername().matches("^[A-Za-z ]+$")) {

            throw new IllegalArgumentException(
                    "Username must contain only letters"
            );
        }

// Checks full name contains only letters and spaces
        if (!updatedUser.getFullName().matches("^[A-Za-z ]+$")) {

            throw new IllegalArgumentException(
                    "FullName must contain only letters"
            );
        }

// Updates username field
        existing.setUsername(updatedUser.getUsername());

        // Updates password field
        existing.setPassword(updatedUser.getPassword());

        // Updates email field
        existing.setEmail(updatedUser.getEmail());

        // Updates full name field
        existing.setFullName(updatedUser.getFullName());

        // Saves updated user into database
        return userRepository.save(existing);
    }

    // Deletes user by ID
    @Override
    public boolean deleteUser(Integer userId) {

        if (!userRepository.existsById(userId)) {

            throw new UserNotFoundException(userId);
        }

        // Checks whether user has role mappings
        if (userRolesRepository.existsByUser_UserID(userId)) {

            throw new UserRoleMappingExistsException(userId);
        }

        userRepository.deleteById(userId);

        return true;
    }
}