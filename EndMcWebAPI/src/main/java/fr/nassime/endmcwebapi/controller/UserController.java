package fr.nassime.endmcwebapi.controller;

import fr.nassime.endmcwebapi.api.User;
import fr.nassime.endmcwebapi.services.UserService;
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
    public void createUser(@RequestParam UUID uuid, @RequestParam String name, @RequestParam Integer coins) {
        userService.createUser(uuid, name, coins);
    }

    @GetMapping
    public List<User> getUser(@RequestParam(required = false) String name, @RequestParam(required = false) UUID uuid) {
        if (name != null) {
            if (userService.getUserByName(name) == null) {
                throw new RuntimeException("User not found");
            }
            return Collections.singletonList(userService.getUserByName(name));
        } else if (uuid != null) {
            if (userService.getUserByUUID(uuid) == null) {
                throw new RuntimeException("User not found");
            }
            return Collections.singletonList(userService.getUserByUUID(uuid));
        } else {
            return userService.findAll();
        }
    }

    @GetMapping("/exists/uuid")
    public boolean existsUserByUUID(@RequestParam UUID uuid) {
        return userService.existsUserByUuid(uuid);
    }

    @PatchMapping("/uuid")
    public void updateUserByUuid(@RequestParam UUID uuid, @RequestBody User user) {
        if (userService.getUserByUUID(uuid) == null) {
            throw new RuntimeException("User not found");
        }
        userService.updateUserByUuid(uuid, user);
    }

    @PatchMapping("/name")
    public void updateUserByName(@RequestParam String name, @RequestBody User user) {
        if (userService.getUserByName(name) == null) {
            throw new RuntimeException("User not found");
        }
        userService.updateUserByName(name, user);
    }

}
