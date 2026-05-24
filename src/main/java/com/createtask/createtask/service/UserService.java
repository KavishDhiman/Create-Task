package com.createtask.createtask.service;

import com.createtask.createtask.entity.AppUser;

import java.util.List;
import java.util.TreeSet;

// Service interface for user-related business operations
public interface UserService {

    // Creates a new user
    AppUser createUser(AppUser user);

    // Retrieves user by user ID
    AppUser getUserById(Integer userId);

    // Retrieves all users as a list
    List<AppUser> getAllUsers();

    // Retrieves all users sorted using TreeSet
    TreeSet<AppUser> getAllUsersSorted();

    // Updates existing user details
    AppUser updateUser(Integer userId, AppUser updatedUser);

    // Deletes user by ID
    boolean deleteUser(Integer userId);
}