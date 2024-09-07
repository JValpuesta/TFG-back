package com.valpuestajorge.conecta4.app_user.controller;

import com.valpuestajorge.conecta4.app_user.domain.AppUser;
import com.valpuestajorge.conecta4.app_user.dto.in.AppUserInputDto;
import com.valpuestajorge.conecta4.app_user.service.AppUserServicePort;
import com.valpuestajorge.conecta4.errors.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "User", description = "User operations")
@RequestMapping("/v1/user")
@AllArgsConstructor
@Getter
public class AppUserController {

    private final AppUserServicePort service;

    @Operation(
            summary = "Find an entity with the received id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Entity retrieved"),
                    @ApiResponse(responseCode = "404", description = "No entity found")
            }
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AppUser>> getById(
            @Parameter(description = "Object ID", required = true) @PathVariable Long id) {
        Mono<AppUser> business = getService().getUserById(id);
        return business.map(ResponseEntity::ok)
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
    public Mono<ResponseEntity<AppUser>> post(@Valid @RequestBody AppUserInputDto inputEntity) throws NotFoundException {

        Mono<AppUser> created = getService().addUser(inputEntity.getUsername(), inputEntity.getIp());
        return created.map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }
}
