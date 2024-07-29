package fr.nassime.endmcwebapi.services;

import fr.nassime.endmcwebapi.api.User;
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
        userRepository.save(user);
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public User getUserByUUID(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

    public boolean existsUserByUuid(UUID uuid) {
        return userRepository.existsByUuid(uuid);
    }

    public void updateUserByUuid(UUID uuid, User userUpdate) {
        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        updateUserData(user, userUpdate);
        userRepository.save(user);
    }

    public void updateUserByName(String name, User user) {
        User userUpdate = userRepository.findByName(name);

        if (userUpdate == null) {
            throw new RuntimeException("User not found");
        }

        updateUserData(userUpdate, user);
        userRepository.save(userUpdate);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private void updateUserData(User user, User updatedUser) {
        if (updatedUser.getCoins() != null) {
            user.setCoins(updatedUser.getCoins());
        }
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
    }

}
