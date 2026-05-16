package com.createtask.createtask.service;

import com.createtask.createtask.entity.User;

import java.util.List;
import java.util.TreeSet;

/**
 * UserService defines the contract for all user-related business operations.
 * Controllers depend on this interface, not the implementation,
 * keeping the layers loosely coupled.
 */
public interface UserService {

    /** Creates a new user after validating username and email uniqueness. */
    User createUser(User user);

    /** Fetches a user by their primary key. Throws UserNotFoundException if absent. */
    User getUserById(Integer userId);

    /** Returns all users as an unordered list. */
    List<User> getAllUsers();

    /**
     * Returns all users sorted by userID using User's compareTo().
     * TreeSet handles ordering automatically on insertion.
     */
    TreeSet<User> getAllUsersSorted();

    /** Updates an existing user's fields. Validates uniqueness of new username and email. */
    User updateUser(Integer userId, User updatedUser);

    /** Deletes a user by ID. Validates existence before deletion. */
    void deleteUser(Integer userId);
}