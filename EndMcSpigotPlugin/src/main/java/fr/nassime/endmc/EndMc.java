package fr.nassime.endmc;

import com.google.gson.Gson;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class EndMc extends JavaPlugin {

    private static EndMc instance;
    private Gson gson;

    @Override
    public void onEnable() {
        instance = this;
        gson = new Gson();
    }

    public static EndMc get() {
        return instance;
    }

}
