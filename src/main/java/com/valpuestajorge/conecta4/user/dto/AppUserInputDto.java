package com.valpuestajorge.conecta4.user.dto;

import com.valpuestajorge.conecta4.shared.restapibusiness.dto.in.BusinessInputDto;
import com.valpuestajorge.conecta4.shared.util.NationalityEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserInputDto extends BusinessInputDto {

    private String email;
    private String username;
    private String password;
    private NationalityEnum nationality;

}
