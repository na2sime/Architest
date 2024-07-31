package fr.nassime.endmc.commands;

import fr.nassime.endmc.api.user.User;
import fr.nassime.endmc.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class UserCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length != 1) {
            commandSender.sendMessage("§cUsage: /user <name>");
            return false;
        }
        Player player = Bukkit.getPlayer(args[0]);

        if(player == null) {
            commandSender.sendMessage("§cThis player is not online.");
            return false;
        }

        User user = UserManager.get().getUser(player.getUniqueId());
        commandSender.sendMessage("§7User informations");
        commandSender.sendMessage("§a" + player.getName() + " has " + user.getCoins() + " coins.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        String lastArg = args.length == 0 ? "" : args[args.length - 1].toLowerCase();
        int size = args.length == 0 ? 0 : args.length - 1;

        if (size == 0) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(lastArg)).toList();
        }
        return List.of();
    }
}
