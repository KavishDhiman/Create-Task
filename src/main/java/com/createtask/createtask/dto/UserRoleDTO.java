package com.createtask.createtask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleDTO {

    @NotNull(message = "UserRoleID must not be null")
    private Integer userRoleID;

    @NotBlank(message = "RoleName must not be blank")
    @Size(max = 255, message = "RoleName must not exceed 255 characters")
    private String roleName;
}