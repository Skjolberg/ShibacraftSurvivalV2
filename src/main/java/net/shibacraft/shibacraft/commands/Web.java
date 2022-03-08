package net.shibacraft.shibacraft.commands;

import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Web implements CommandExecutor {

    private final Shibacraft plugin;

    public Web(Shibacraft plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command shibacraft, String label, String[] args) {

        YamlManager messagesFile = new YamlManager(plugin, "messages");

        sender.sendMessage(Utils.toLegacyColors(messagesFile.getString("Web").replace("{prefix}", Utils.getPrefixMessages())));
        return true;
    }
}
