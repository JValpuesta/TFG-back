package com.valpuestajorge.conecta4.security.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderWrapper extends BCryptPasswordEncoder implements PasswordEncoderWrapper {
    public String cypherPassword(String username, String password ){
        String hashedPassword =  password + "_" + username;
        hashedPassword = HashUtils.getHash(hashedPassword);
        return super.encode(hashedPassword);
    }
}
