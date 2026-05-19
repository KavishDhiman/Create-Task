package com.createtask.createtask.service;

import com.createtask.createtask.dto.response.UserProductivityDTO;

import java.util.List;

/**
 * ReportService defines the contract for all high-impact reporting operations.
 * Kept separate from TaskService and UserService to follow single responsibility.
 */
public interface ReportService {

    /**
     * Generates a productivity report for every user.
     */
    List<UserProductivityDTO> getUserProductivityReport();
}