package net.shibacraft.shibacraft.commands;


import me.clip.placeholderapi.PlaceholderAPI;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.utils.Utils;
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

        YamlManager messagesFile = new YamlManager(plugin, "messages");

        if (!(sender instanceof Player user)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    messagesFile.getString("ConsoleSender").replace("{prefix}", Utils.getPrefixMessages())));
        } else {

            YamlManager fileManager = new YamlManager(plugin, "wallet");

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
                        user.sendMessage(Utils.toLegacyColors(
                                messagesFile.getString("messages.InvalidArgument").replace("{prefix}", Utils.getPrefixMessages())));
                        return true;
                    }
                }
            } else {
                List<String> sectionList;
                sectionList = fileManager.getStringList("1");
                for (String i : sectionList) {
                    String placeHolder = Utils.toLegacyColors(
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
