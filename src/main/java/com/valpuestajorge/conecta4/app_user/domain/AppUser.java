package com.valpuestajorge.conecta4.app_user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    private Long id;
    @Column
    private LocalDateTime createdDate;
    @Column
    private String username;
    @Column
    private String ip;

    public AppUser(String username, String ip){
        this.createdDate = LocalDateTime.now();
        this.username = username;
        this.ip = ip;
    }
}