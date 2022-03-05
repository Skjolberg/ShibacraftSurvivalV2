package net.shibacraft.shibacraft.commands;


import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Ayuda implements CommandExecutor {

    private final Shibacraft plugin;

    public Ayuda(Shibacraft plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command ayuda, String label, String[] args) {

        Player user = (Player) sender;
        YamlManager ayudaFile = new YamlManager(plugin, "ayuda");
        YamlManager messagesFile = new YamlManager(plugin, "messages");
        final String prefix = messagesFile.getString("Prefix");

        if (args.length > 0) {
            for (String fileSection : ayudaFile.getKeys(false)) {
                if (args[0].equals(fileSection)) {
                    List<String> sectionList;
                    sectionList = ayudaFile.getStringList(fileSection);
                    for (String i : sectionList) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                i));
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
            sectionList = ayudaFile.getStringList("1");
            for (String i : sectionList) {
                user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        i));
            }
            return true;
        }
        return false;
    }
}



