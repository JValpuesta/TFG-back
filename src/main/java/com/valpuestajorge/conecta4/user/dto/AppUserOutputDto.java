package com.valpuestajorge.conecta4.user.dto;

import com.valpuestajorge.conecta4.shared.restapibusiness.dto.out.BusinessOutputDto;
import com.valpuestajorge.conecta4.shared.util.NationalityEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class AppUserOutputDto extends BusinessOutputDto {

    private String email;
    private String username;
    private NationalityEnum nationality;
    private String ip;

}
