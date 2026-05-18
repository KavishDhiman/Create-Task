package com.createtask.createtask.service;

import com.createtask.createtask.entity.AppUser;

import java.util.List;
import java.util.TreeSet;

/**
 * UserService defines the contract for all user-related business operations.
 * Controllers depend on this interface, not the implementation,
 * keeping the layers loosely coupled.
 */
public interface UserService {

    /** Creates a new user after validating username and email uniqueness. */
    AppUser createUser(AppUser user);

    /** Fetches a user by their primary key. Throws UserNotFoundException if absent. */
    AppUser getUserById(Integer userId);

    /** Returns all users as an unordered list. */
    List<AppUser> getAllUsers();

    /**
     * Returns all users sorted by userID using User's compareTo().
     * TreeSet handles ordering automatically on insertion.
     */
    TreeSet<AppUser> getAllUsersSorted();

    /** Updates an existing user's fields. Validates uniqueness of new username and email. */
    AppUser updateUser(Integer userId, AppUser updatedUser);

    /**
     * Deletes a user by ID and returns true if deletion was successful.
     * Throws UserNotFoundException if the user does not exist.
     */
    boolean deleteUser(Integer userId);
}