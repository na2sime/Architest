package fr.nassime.endmc;

import com.google.gson.Gson;
import fr.nassime.endmc.commands.UserCommand;
import fr.nassime.endmc.listener.PlayerListeners;
import fr.nassime.endmc.manager.UserManager;
import fr.nassime.endmc.web.WebBridge;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public class EndMc extends JavaPlugin {

    private static EndMc instance;
    private Gson gson;
    private WebBridge webBridge;

    @Override
    public void onEnable() {
        instance = this;
        gson = new Gson();

        webBridge = new WebBridge("http://localhost:8080/");
        new UserManager();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListeners(), this);

        Objects.requireNonNull(getCommand("user")).setExecutor(new UserCommand());
        Objects.requireNonNull(getCommand("user")).setTabCompleter(new UserCommand());

        getLogger().info("EndMc has been enabled!");
    }

    public static EndMc get() {
        return instance;
    }
}
