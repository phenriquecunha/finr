package my.finr.finr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import my.finr.finr.dto.AuthDTO;
import my.finr.finr.dto.RegisterDTO;
import my.finr.finr.exceptionHandling.ResponseError;
import my.finr.finr.exceptionHandling.UserExistsException;
import my.finr.finr.model.User;
import my.finr.finr.service.JWTService;
import my.finr.finr.service.RegisterService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private RegisterService registerService;

    @Autowired
    JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthDTO dto) {
        try {
            UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(dto.nickname(),
                    dto.password());
            var auth = this.authManager.authenticate(userAuthToken);
            String token = jwtService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseError(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO dto) {

        try {
            registerService.registerUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserExistsException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                            "User '" + dto.nickname() + "' already exists"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                            "Role '" + dto.role() + "' is invalid role!"));
        }

    }
}
