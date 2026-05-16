package com.createtask.createtask.exception;

// Thrown when a project with the given ID doesn't exist in the database.
// Extends RuntimeException so we don't need to declare it in every method signature.


public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(String message) {
        super(message);
    }

    public ProjectNotFoundException(Integer projectID) {
        super("Project with ID " + projectID + " was not found.");
    }
}