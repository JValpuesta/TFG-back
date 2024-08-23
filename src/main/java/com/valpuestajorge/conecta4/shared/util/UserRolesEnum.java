package com.valpuestajorge.conecta4.shared.util;

public enum UserRolesEnum {
    ROLE_ADMIN("admin"),
    ROLE_USER("user"),
    ROLE_INVITED("invited");

    private final String description;

    UserRolesEnum(String colorName) {
        this.description = colorName;
    }

    public String getDescription() {
        return description;
    }
}
