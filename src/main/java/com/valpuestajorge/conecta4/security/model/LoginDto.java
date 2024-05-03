package com.valpuestajorge.conecta4.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record LoginDto(@Schema(defaultValue = "user") String username,
                       @Schema(defaultValue = "password") String password) {
}
