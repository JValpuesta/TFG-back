package com.valpuestajorge.conecta4.shared.restapibusiness.entity.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Business {
    private Long id;
    private LocalDateTime createdDate;
    private String origin;
}
