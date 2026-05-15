package com.createtask.createtask.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRolesDTO {

    @NotNull(message = "UserID must not be null")
    private Integer userID;

    @NotNull(message = "UserRoleID must not be null")
    private Integer userRoleID;
}