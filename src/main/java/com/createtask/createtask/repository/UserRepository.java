package com.createtask.createtask.repository;

import com.createtask.createtask.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for AppUser database operations
@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> { // Extends JpaRepository for CRUD operations

    // Checks whether username already exists
    boolean existsByUsername(String username);

    // Checks whether email already exists
    boolean existsByEmail(String email);
}