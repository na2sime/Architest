package fr.nassime.endmcwebapi.controller;

import fr.nassime.endmcwebapi.api.User;
import fr.nassime.endmcwebapi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestParam UUID uuid, @RequestParam String name, @RequestParam Integer coins) {
        userService.createUser(uuid, name, coins);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@RequestParam(required = false) String name, @RequestParam(required = false) UUID uuid) {
        if (name != null) {
            if (userService.getUserByName(name) == null) {
                throw new RuntimeException("User not found");
            }
            return userService.getUserByName(name);
        } else if (uuid != null) {
            if (userService.getUserByUUID(uuid) == null) {
                throw new RuntimeException("User not found");
            }
            return userService.getUserByUUID(uuid);
        }
        return null;
    }

    @PatchMapping()
    public void updateUserByName(@RequestParam UUID uuid, @RequestBody User user) {
        if (userService.getUserByUUID(uuid) == null) {
            throw new RuntimeException("User not found");
        }
        userService.updateUserByUuid(uuid, user);
    }

}
