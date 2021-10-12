package com.github.gimaz.testcasevitasoft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private String username;
    private String roleName;
}
