package edu.tucn.scd.serverapp.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Naicu Tudor
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Create a user")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userService.createUser(userDTO);
    }

    @GetMapping()
    @Operation(summary = "Get a user by username")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDTO getUser(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping()
    @Operation(summary = "Update a user's password by username")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDTO updateUserPassword(@RequestParam String username, @RequestParam String newPassword) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserDTO userDTO = userService.getUserByUsername(username);
        if (userDTO != null) {
            userDTO.setPassword(passwordEncoder.encode(newPassword));
            return userService.updateUser(userDTO);
        }
        return null; // or handle the case where the user doesn't exist

    }

    @DeleteMapping()
    @Operation(summary = "Delete a user by username")
    @SecurityRequirement(name = "Bearer Authentication")
    public void deleteUser(@RequestParam String username) {
        userService.deleteUser(username);
    }

}
