package net.shibacraft.shibacraft.commands;

import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Wiki implements CommandExecutor {

    private final Shibacraft plugin;

    public Wiki(Shibacraft plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command shibacraft, String label, String[] args) {

        YamlManager messagesFile = new YamlManager(plugin, "messages");

        sender.sendMessage(Utils.toLegacyColors(messagesFile.getString("Wiki").replace("{prefix}", Utils.getPrefixMessages())));
        return true;
    }
}
