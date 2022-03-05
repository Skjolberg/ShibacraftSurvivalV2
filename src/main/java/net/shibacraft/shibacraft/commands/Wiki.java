package net.shibacraft.shibacraft.commands;

import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Wiki implements CommandExecutor {
    private final Shibacraft plugin;

    public Wiki(Shibacraft plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command shibacraft, String label, String[] args) {
        Player user = (Player) sender;
        YamlManager messagesFile = new YamlManager(plugin, "messages");
        final String prefix = messagesFile.getString("Prefix");
        if (args.length > 0) {
            user.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', messagesFile.getString("NoArguments2").replace("{prefix}", prefix)));
        } else {
            user.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', messagesFile.getString("Wiki").replace("{prefix}", prefix)));
        }
        return true;
    }
}
