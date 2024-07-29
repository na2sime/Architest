package fr.nassime.endmc.manager;

import fr.nassime.endmc.EndMc;
import fr.nassime.endmc.api.User;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final Map<UUID, User> users = new HashMap<>();
    private static UserManager instance;

    private static final String API_USER = "user";
    private static final String API_PASSWORD = "password";

    public UserManager() {
        instance = this;
    }

    public void loadUser(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(EndMc.get(), () -> {
            try {
                URL url = new URL("http://localhost:4000/user");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String userCredentials = API_USER + ":" + API_PASSWORD;
                String basicAuth = "Basic " + new String(
                        java.util.Base64.getEncoder().encode(userCredentials.getBytes()));

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", basicAuth);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                User user = EndMc.get().getGson().fromJson(reader, User.class);
                users.put(uuid, user);
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
