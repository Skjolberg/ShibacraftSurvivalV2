package net.shibacraft.shibacraft.commands;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.dependencies.LuckPermsDependency;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.service.ShibacraftService;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ShibacraftCommand implements CommandExecutor {

    private final net.shibacraft.shibacraft.Shibacraft plugin;
    private final net.luckperms.api.LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final LuckPermsDependency luckPermsDependency;
    private final ShibacraftService shibacraftService;

    public ShibacraftCommand(Shibacraft plugin, LuckPermsDependency luckPermsDependency, ShibacraftService shibacraftService) {

        this.plugin = plugin;
        this.luckPermsDependency = luckPermsDependency;
        this.shibacraftService = shibacraftService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command shibacraft, String label, String[] args) {


        YamlManager messagesFile = new YamlManager(plugin, "messages");
        YamlManager ayudaFile = new YamlManager(plugin, "ayuda");
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        YamlManager walletFile = new YamlManager(plugin, "wallet");

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                messagesFile.reload();
                ayudaFile.reload();
                ciudadesFile.reload();
                walletFile.reload();
                if (messagesFile.getString("PluginReload").trim().length() > 0) {
                    sender.sendMessage(Utils.toLegacyColors(
                            messagesFile.getString("PluginReload").replace("{prefix}", Utils.getPrefixMessages())));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("city") && args[1].equalsIgnoreCase("delete") && args.length > 2) {
                if (args[2].equalsIgnoreCase("all")) {
                    shibacraftService.deleteCitys(sender);
                } else {
                    shibacraftService.deleteCity(sender, args[2]);
                }
                return true;
            } else {
                sender.sendMessage(Utils.toLegacyColors(messagesFile.getString("InvalidArgument").replace("{prefix}", Utils.getPrefixMessages())));
            }
            sender.sendMessage(Utils.toLegacyColors(messagesFile.getString("InvalidArgument").replace("{prefix}", Utils.getPrefixMessages())));
        } else {
            sender.sendMessage(Utils.toLegacyColors(messagesFile.getString("NoArguments").replace("{prefix}", Utils.getPrefixMessages())));
        }
        return true;
    }
}
