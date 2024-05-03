package com.valpuestajorge.conecta4.security.model;

import java.util.List;
public record UserSecurityOutputDto(String id, String username, String email, Boolean requiredPasswordChangeFlag,
                                    Boolean enabled, List<String> roles) {
}