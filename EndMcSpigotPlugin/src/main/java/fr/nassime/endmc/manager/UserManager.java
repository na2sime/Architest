package fr.nassime.endmc.manager;

import fr.nassime.endmc.EndMc;
import fr.nassime.endmc.api.user.User;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class UserManager {

    private final Map<UUID, User> users = new HashMap<>();
    private static UserManager instance;
    private final EndMc plugin = EndMc.get();

    public UserManager() {
        instance = this;
    }

    public void handleJoin(Player player) {
        loadUser(player);
    }

    public void handleQuit(Player player) {
        saveUser(player.getUniqueId());
        removeUser(player.getUniqueId());
    }

    public void saveUser(UUID uuid) {
        User user = users.get(uuid);
        updateUser(user).whenComplete((updatedUser, error) -> {
            if (error != null) {
                return;
            }
            users.put(uuid, updatedUser);
        });
    }

    private void loadUser(Player player) {
        getUserFromWeb(player.getUniqueId()).whenComplete((user, error) -> {
            if (error != null) {
                return;
            }
            User data = user;
            if (user == null) {
                data = new User(player.getUniqueId(), player.getName(), 100);
                createUser(data);
            }
            users.put(player.getUniqueId(), data);
        });
    }

    private void createUser(User user) {
        String data = plugin.getGson().toJson(user);

        plugin.getWebBridge().executeRequest("user",
                builder -> builder.POST(HttpRequest.BodyPublishers.ofString(data, StandardCharsets.UTF_8)));
    }

    private CompletableFuture<User> getUserFromWeb(UUID uniqueId) {
        return plugin.getWebBridge().executeRequest("user/" + uniqueId, HttpRequest.Builder::GET);
    }

    private CompletableFuture<User> updateUser(User user) {
        String data = plugin.getGson().toJson(user);
        return plugin.getWebBridge().executeRequest("user",
                builder -> builder.method("PATCH", HttpRequest.BodyPublishers.ofString(data, StandardCharsets.UTF_8)));
    }

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public void removeUser(UUID uuid) {
        users.remove(uuid);
    }

    public static UserManager get() {
        return instance;
    }

}
