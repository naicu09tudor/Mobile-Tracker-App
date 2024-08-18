package edu.tucn.scd.serverapp.security;

import edu.tucn.scd.serverapp.user.UserDTO;
import edu.tucn.scd.serverapp.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LoginController handles user authentication.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    public LoginController(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user based on provided credentials.
     *
     * @param credentialsDTO The user credentials.
     * @return A JWT token if authentication is successful, Unauthorized response otherwise.
     */
    @PostMapping()
    public ResponseEntity<JwtTokenDTO> login(@RequestBody CredentialsDTO credentialsDTO) {
        String username = credentialsDTO.getUsername();
        String rawPassword = credentialsDTO.getPassword();

        UserDTO user = userService.getUserByUsername(username);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
            String generatedToken = JwtUtil.generateToken(username);
            jwtTokenDTO.setToken(generatedToken);

            return ResponseEntity.ok(jwtTokenDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}