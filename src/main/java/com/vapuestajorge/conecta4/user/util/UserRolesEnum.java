package com.vapuestajorge.conecta4.user.util;

public enum UserRolesEnum {
    ADMIN("admin"),
    LOGIN("login"),
    INVITED("invited");

    private final String description;

    UserRolesEnum(String colorName) {
        this.description = colorName;
    }

    public String getDescription() {
        return description;
    }
}
