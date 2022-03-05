package net.shibacraft.shibacraft.commands;


import me.clip.placeholderapi.PlaceholderAPI;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Wallet implements CommandExecutor {

    private final Shibacraft plugin;

    public Wallet(Shibacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command ayuda, String label, String[] args) {

        if (sender == null) {
            Bukkit.getConsoleSender().sendMessage(
                    "[" + plugin.nombre + "]" + ChatColor.RED + " You cant run this command from the console.");
            return false;
        } else {
            Player user = (Player) sender;
            YamlManager fileManager = new YamlManager(plugin, "wallet");
            YamlManager messagesFile = new YamlManager(plugin, "messages");
            final String prefix = messagesFile.getString("Prefix");

            if (args.length > 0) {
                for (String fileSection : fileManager.getKeys(false)) {
                    if (args[0].equals(fileSection)) {
                        List<String> sectionList;
                        sectionList = fileManager.getStringList(fileSection);
                        for (String i : sectionList) {
                            String placeHolder = ChatColor.translateAlternateColorCodes('&',
                                    i);
                            placeHolder = PlaceholderAPI.setPlaceholders(user.getPlayer(), placeHolder);
                            user.sendMessage(placeHolder);
                        }
                        return true;
                    } else if (args.length > 1) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                messagesFile.getString("messages.InvalidArgument").replace("{prefix}", prefix)));
                        return true;
                    }
                }
            } else {
                List<String> sectionList;
                sectionList = fileManager.getStringList("1");
                for (String i : sectionList) {
                    String placeHolder = ChatColor.translateAlternateColorCodes('&',
                            i);
                    placeHolder = PlaceholderAPI.setPlaceholders(user.getPlayer(), placeHolder);
                    user.sendMessage(placeHolder);
                }
                return true;
            }
        }
        return false;
    }
}
