package my.finr.finr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import my.finr.finr.enumeration.RoleEnum;
import my.finr.finr.exceptionHandling.ResponseError;
import my.finr.finr.exceptionHandling.UserNotFoundException;
import my.finr.finr.model.User;
import my.finr.finr.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Object> getUsers(@RequestParam(name = "role", required = false) String role) {
        try {
            List<User> users;
            if (role != null) {
                users = userService.findAll(RoleEnum.valueOf(role.toUpperCase()));
            } else {
                users = userService.findAll();
            }
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                            "Role '" + role + "' is invalid role!"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsersById(@PathVariable Long id) {
        try {
            User user;
            user = userService.findById(id);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                            "User ID: '" + id + "' not found!"));
        }
    }
}
