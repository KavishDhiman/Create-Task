package com.createtask.createtask.service.impl;

import com.createtask.createtask.entity.AppUser;
import com.createtask.createtask.exception.DuplicateUserException;
import com.createtask.createtask.exception.UserNotFoundException;
import com.createtask.createtask.repository.UserRepository;
import com.createtask.createtask.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

/**
 * UserServiceImpl provides the concrete business logic for all user operations.
 * Uses constructor injection for the repository dependency.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates that username and email are not already taken before saving.
     * Throws DuplicateUserException for either conflict.
     */
    @Override
    public AppUser createUser(AppUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUserException("username", user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateUserException("email", user.getEmail());
        }
        return userRepository.save(user);
    }

    /**
     * Uses Optional.orElseThrow to avoid returning null
     * and ensure a proper exception is raised when the user is missing.
     */
    @Override
    public AppUser getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /** Returns all users from the database as an unordered list. */
    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * TreeSet automatically calls User's compareTo() on each insertion,
     * producing a set sorted by userID without an explicit sort call.
     */
    @Override
    public TreeSet<AppUser> getAllUsersSorted() {
        return new TreeSet<>(userRepository.findAll());
    }

    /**
     * Fetches the existing record first to confirm it exists.
     * Skips uniqueness check on username/email if the value has not changed,
     * to avoid a false conflict when updating other fields only.
     */
    @Override
    public AppUser updateUser(Integer userId, AppUser updatedUser) {
        AppUser existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!existing.getUsername().equals(updatedUser.getUsername())
                && userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new DuplicateUserException("username", updatedUser.getUsername());
        }
        if (!existing.getEmail().equals(updatedUser.getEmail())
                && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new DuplicateUserException("email", updatedUser.getEmail());
        }

        existing.setUsername(updatedUser.getUsername());
        existing.setPassword(updatedUser.getPassword());
        existing.setEmail(updatedUser.getEmail());
        existing.setFullName(updatedUser.getFullName());

        return userRepository.save(existing);
    }

    /**
     * Validates user exists before deletion.
     * Returns true after successful deletion to confirm the operation completed.
     * Throws UserNotFoundException if the ID does not exist.
     */
    @Override
    public boolean deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
        return true;
    }
}