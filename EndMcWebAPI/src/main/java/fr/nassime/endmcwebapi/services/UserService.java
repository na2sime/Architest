package fr.nassime.endmcwebapi.services;

import fr.nassime.endmcwebapi.api.User;
import fr.nassime.endmcwebapi.exceptions.CreateException;
import fr.nassime.endmcwebapi.exceptions.ExecutionException;
import fr.nassime.endmcwebapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UUID uuid, String name, Integer coins) {
        User user = new User();
        user.setUuid(uuid);
        user.setName(name);
        user.setCoins(coins);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new CreateException("User already exists");
        }
    }

    public User getUserByName(String name) {
        try {
            return userRepository.findByName(name);
        } catch (Exception e) {
            throw new ExecutionException("User not found");
        }
    }

    public User getUserByUUID(UUID uuid) {
        try {
            return userRepository.findByUuid(uuid);
        } catch (Exception e) {
            throw new ExecutionException("User not found");
        }
    }

    public void updateUserByUuid(UUID uuid, User userUpdate) {
        try {
            User user = userRepository.findByUuid(uuid);
            if (user == null) {
                throw new ExecutionException("User not found");
            }
            updateUserData(user, userUpdate);
            userRepository.save(user);
        } catch (Exception e) {
            throw new ExecutionException("User not found");
        }
    }

    private void updateUserData(User user, User updatedUser) {
        try {
            if (updatedUser.getCoins() != null) {
                user.setCoins(updatedUser.getCoins());
            }
            if (updatedUser.getName() != null) {
                user.setName(updatedUser.getName());
            }
        } catch (Exception e) {
            throw new ExecutionException("User not found");

        }
    }

}
