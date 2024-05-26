package com.valpuestajorge.conecta4.shared.restapibusiness.controller;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.shared.restapibusiness.dto.in.BusinessInputDto;
import com.valpuestajorge.conecta4.shared.restapibusiness.dto.in.BusinessInputDtoMapper;
import com.valpuestajorge.conecta4.shared.restapibusiness.dto.out.BusinessOutputDto;
import com.valpuestajorge.conecta4.shared.restapibusiness.dto.out.BusinessOutputDtoMapper;
import com.valpuestajorge.conecta4.shared.restapibusiness.entity.persistence.BusinessEntity;
import com.valpuestajorge.conecta4.shared.restapibusiness.service.BusinessServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1")
@CrossOrigin("*")
public abstract class BusinessController<B extends BusinessEntity, O extends BusinessOutputDto,
        I extends BusinessInputDto> {
    protected abstract BusinessServicePort<B> getService();

    protected abstract BusinessOutputDtoMapper<B, O> getOutputMapper();

    protected abstract BusinessInputDtoMapper<B, I> getInputMapper();

    @Autowired
    private MessageSource messageSource;

    @Operation(
            summary = "Find an entity with the received id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Entity retrieved"),
                    @ApiResponse(responseCode = "404", description = "No entity found")
            }
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<O>> getById(
            @Parameter(description = "Object ID", required = true) @PathVariable Long id) {
        Mono<B> business = getService().getById(id);
        Mono<O> output = business.map(getOutputMapper()::toSecond);
        return output.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new entity",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Entity created"),
                    @ApiResponse(responseCode = "400", description = "Invalid fields sent"),
                    @ApiResponse(responseCode = "500", description = "Internal creation error")
            }
    )
    @PostMapping(value = "", produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
    public Mono<ResponseEntity<O>> post(@Valid @RequestBody I inputEntity) throws NotFoundException {

        B domain = getInputMapper().toFirst(inputEntity);
        Mono<B> created = getService().post(domain);
        Mono<O> output = created.map(getOutputMapper()::toSecond);
        return output.map(businessOutputDto -> ResponseEntity.status(HttpStatus.CREATED).body(businessOutputDto));
    }

    @Operation(
            summary = "Update an entity",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Entity updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid fields"),
                    @ApiResponse(responseCode = "404", description = "Entity not found"),
                    @ApiResponse(responseCode = "500", description = "Internal update error")
            }
    )
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<O>> patch(@PathVariable("id") Long id, @RequestBody Map<String, Object> inputMap)
            throws ParseException, ClassNotFoundException {

        return getService().patch(id, inputMap).map(getOutputMapper()::toSecond).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete an entity with the received id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Entity deleted"),
                    @ApiResponse(responseCode = "404", description = "No entity found"),
                    @ApiResponse(responseCode = "500", description = "Bad Request")
            }
    )
    @DeleteMapping("/{id}")
    public Mono<String> delete(@PathVariable Long id) throws NotFoundException {

        Locale locale = LocaleContextHolder.getLocale();
        String deleteMessage = messageSource.getMessage("business.delete", null, locale);
        return getService().delete(id).thenReturn(deleteMessage + id);
    }

    /*@GetMapping
    public Mono<ResponseEntity<Page<O>>> getAllBusinesses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return getService().getAll(PageRequest.of(page, size))
                .map(getOutputMapper()::toSecond)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }*/
}
