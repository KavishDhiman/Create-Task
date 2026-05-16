package com.createtask.createtask.service.impl;

import com.createtask.createtask.entity.User;
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
    public User createUser(User user) {
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
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * TreeSet automatically calls User's compareTo() on each insertion,
     * producing a set sorted by userID without an explicit sort call.
     */
    @Override
    public TreeSet<User> getAllUsersSorted() {
        return new TreeSet<>(userRepository.findAll());
    }

    /**
     * Fetches the existing record first to confirm it exists.
     * Skips uniqueness check on username/email if the value has not changed,
     * to allow a user to update other fields without triggering a false conflict.
     */
    @Override
    public User updateUser(Integer userId, User updatedUser) {
        User existing = userRepository.findById(userId)
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
     * Checks existence before deletion so that a missing ID returns a clear
     * UserNotFoundException rather than silently doing nothing.
     */
    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
    }
}
