package com.valpuestajorge.conecta4.shared.restapibusiness.dto.in;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Getter
@Setter
public abstract class BusinessInputDto {
    @Autowired
    protected MessageSource messageSource;
    protected Locale locale = LocaleContextHolder.getLocale();
}
