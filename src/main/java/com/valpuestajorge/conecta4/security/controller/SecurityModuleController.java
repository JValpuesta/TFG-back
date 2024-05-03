package com.valpuestajorge.conecta4.security.controller;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import com.valpuestajorge.conecta4.errors.UnprocessableEntityException;
import com.valpuestajorge.conecta4.security.model.ChangePasswordDto;
import com.valpuestajorge.conecta4.security.model.LoginDto;
import com.valpuestajorge.conecta4.security.model.LoginOutputDto;
import com.valpuestajorge.conecta4.security.service.ISecurityService;
import com.valpuestajorge.conecta4.user.business.AppUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Security", description = "AppUser management operations")
@RequestMapping("/security/user")
@CrossOrigin("*")
@AllArgsConstructor
public class SecurityModuleController {

    private final ISecurityService securityService;
    //private final UserOutputDtoMapper outputMapper;
    //private final UserInputDtoMapper inputMapper;

    @PostMapping("/login/")
    public ResponseEntity<LoginOutputDto> login(@RequestBody LoginDto loginDto) throws NotFoundException, UnprocessableEntityException {
        LoginOutputDto result = securityService.login(loginDto.username(), loginDto.password());
        if (result.status().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(result);
        } else if (result.status().equals(HttpStatus.BAD_REQUEST)) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else if (result.status().equals(HttpStatus.UNAUTHORIZED)) {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{username}")
    public ResponseEntity<Mono<AppUser>> searchUser(@PathVariable String username) {
        //return ResponseEntity.ok(outputMapper.toSecond(securityService.searchUserByUsername(username)));
        return ResponseEntity.ok(securityService.searchUserByUsername(username));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('user', 'admin')")
    @PatchMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto) throws NotFoundException, UnprocessableEntityException {
        securityService.changePassword(changePasswordDto);
        return ResponseEntity.ok("Password updated");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('admin')")
    @PatchMapping("/updateUserAvailability/{username}")
    public ResponseEntity<String> updateUserAvailability(@PathVariable String username, @RequestBody Boolean available) throws NotFoundException, UnprocessableEntityException {
        securityService.updateUserAvailability(username, available);
        return ResponseEntity.ok("AppUser updated");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/lostPassword/{username}")
    public ResponseEntity<String> lostPassword(@PathVariable(name = "username") String userName) throws NotFoundException, UnprocessableEntityException {
        securityService.lostPassword(userName);
        return ResponseEntity.ok("Password replaced");
    }
}
