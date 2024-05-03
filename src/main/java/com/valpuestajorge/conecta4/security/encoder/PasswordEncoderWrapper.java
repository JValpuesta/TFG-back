package com.valpuestajorge.conecta4.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface PasswordEncoderWrapper extends PasswordEncoder {
     String cypherPassword(String username, String password );
}
