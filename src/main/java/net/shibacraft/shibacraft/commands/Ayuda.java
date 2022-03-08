package net.shibacraft.shibacraft.commands;


import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.Console;
import java.util.List;

public class Ayuda implements CommandExecutor {

    private final Shibacraft plugin;

    public Ayuda(Shibacraft plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command ayuda, String label, String[] args) {

        YamlManager ayudaFile = new YamlManager(plugin, "ayuda");
        YamlManager messagesFile = new YamlManager(plugin, "messages");

        List<String> sectionList;
        if (args.length > 0) {
            if (ayudaFile.contains(args[0]) && args.length == 1) {
                sectionList = ayudaFile.getStringList(args[0]);
                for (String i : sectionList) {
                    sender.sendMessage(Utils.toLegacyColors(
                            i));
                }
            } else {
                sender.sendMessage(Utils.toLegacyColors(
                        messagesFile.getString("InvalidArgument").replace("{prefix}", Utils.getPrefixMessages())));
            }
        } else {
            sectionList = ayudaFile.getStringList("1");
            for (String i : sectionList) {
                sender.sendMessage(Utils.toLegacyColors(
                        i));
            }
        }
        return true;
    }
}



