package com.vapuestajorge.conecta4.keycloak.controller;

import com.vapuestajorge.conecta4.errors.NotFoundException;
import com.vapuestajorge.conecta4.errors.UnprocessableEntityException;
import com.vapuestajorge.conecta4.keycloak.model.ChangePasswordDto;
import com.vapuestajorge.conecta4.keycloak.model.LoginDto;
import com.vapuestajorge.conecta4.keycloak.model.LoginOutputDto;
import com.vapuestajorge.conecta4.keycloak.service.ISecurityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@Tag(name = "Security", description = "User management operations")
@RequestMapping("/security/user")
@CrossOrigin("*")
@AllArgsConstructor
public class SecurityModuleController {

    private final ISecurityService securityService;

    //private final UserInputDtoMapper inputMapper;

    @PostMapping("/login/")
    public ResponseEntity<LoginOutputDto> login(@RequestBody LoginDto loginDto) throws NotFoundException, UnprocessableEntityException {
        LoginOutputDto result = securityService.login(loginDto.username(),loginDto.password());
        if(result.status().equals(HttpStatus.OK)){
            return ResponseEntity.ok(result);
        }
        else if(result.status().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        else if(result.status().equals(HttpStatus.UNAUTHORIZED)){
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
        else{
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('admin-client-role')")
    @GetMapping("/")
    public ResponseEntity<List<UserRepresentation>> findAllUsers(){
        return ResponseEntity.ok(securityService.findAllUsers());
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('admin-client-role')")
    @GetMapping("/{username}")
    public ResponseEntity<UserRepresentation> searchUser(@PathVariable String username){
        return ResponseEntity.ok(securityService.searchUserByUsername(username));
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('user-client-role', 'admin-client-role')")
    @GetMapping("/roles/{username}")
    public ResponseEntity<List<String>> getRoles(@PathVariable String username){
        return ResponseEntity.ok(securityService.getRoles(username));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('admin-client-role')")
    @PostMapping("/createRole/{name}")
    public ResponseEntity<String> createRole(@PathVariable String name) {
        securityService.createRole(name);
        return ResponseEntity.ok("Role created");
    }
    /*@SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('admin-client-role')")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody UserInputDto userDto) throws NotFoundException, ParseException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        securityService.updateUser(userId, inputMapper.toFirst(userDto));
        return ResponseEntity.ok("User updated successfully");
    }*/

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('admin-client-role')")
    @PatchMapping("/{userName}/role/{role}")
    public ResponseEntity<String> updateUser(@PathVariable String userName, @PathVariable String role ) {
        securityService.setRole(userName, role);
        return ResponseEntity.ok("User updated successfully");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('admin-client-role')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        securityService.deleteUser(userId);
        return ResponseEntity.ok("User " + userId + " deleted");
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('user-client-role', 'admin-client-role')")
    @PatchMapping("/change_password")
    public ResponseEntity<String> changePassword( @RequestBody ChangePasswordDto changePasswordDto) throws NotFoundException {
        securityService.changePassword(changePasswordDto);
        return ResponseEntity.ok("Password updated");
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('admin-client-role')")
    @PatchMapping("/updateUserAvailability/{username}")
    public ResponseEntity<String> updateUserAvailability( @PathVariable String username, @RequestBody Boolean available) throws NotFoundException {
        securityService.updateUserAvailability(username, available);
        return ResponseEntity.ok("User updated");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/lostPassword/{username}")
    public ResponseEntity<String> lostPassword( @PathVariable(name = "username") String userName) throws NotFoundException {
        securityService.lostPassword(userName);
        return ResponseEntity.ok("Password replaced");
    }

    @GetMapping("/certs")
    public ResponseEntity<String> getCerts() {
        ResponseEntity<String> response = securityService.getCerts();
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

}
