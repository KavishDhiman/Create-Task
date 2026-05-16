package com.createtask.createtask.repository;

import com.createtask.createtask.entity.Notification; // The entity this repository manages
import org.springframework.data.jpa.repository.JpaRepository; // Provides CRUD operations automatically
import org.springframework.stereotype.Repository; // Marks this as a Spring-managed repository bean

import java.util.List; // Return type for multiple notifications

// Spring Data JPA repository for Notification entity
// JpaRepository<Notification, Integer> provides save, findById, findAll, deleteById, etc.
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    // Fetch all notifications for a specific user ordered by newest first
    // Spring automatically generates query from method name
    List<Notification> findByUser_UserIDOrderByCreatedAtDesc(int userId);

}