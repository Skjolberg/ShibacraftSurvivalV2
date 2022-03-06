package net.shibacraft.shibacraft.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Shibacraft implements CommandExecutor {

    private final net.shibacraft.shibacraft.Shibacraft plugin;
    private final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final Utils utils;

    public Shibacraft(net.shibacraft.shibacraft.Shibacraft plugin, Utils utils) {

        this.plugin = plugin;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command shibacraft, String label, String[] args) {

        Player user = (Player) sender;
        YamlManager messagesFile = new YamlManager(plugin, "messages");
        YamlManager ayudaFile = new YamlManager(plugin, "ayuda");
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        YamlManager walletFile = new YamlManager(plugin, "wallet");
        final String prefix = messagesFile.getString("Prefix");
        List<String> ciudadanos;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                messagesFile.reload();
                ayudaFile.reload();
                ciudadesFile.reload();
                walletFile.reload();
                if (messagesFile.getString("PluginReload").trim().length() > 0) {
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            messagesFile.getString("PluginReload").replace("{prefix}", prefix)));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("city") && args[1].equalsIgnoreCase("delete") && args.length > 2) {
                if (args[2].equalsIgnoreCase("all")) {
                    for (String i : ciudadesFile.getKeys(false)) {
                        String suffix = "&f[&b" + ciudadesFile.getString(i + ".displayname") + "&f]&r";
                        ciudadanos = ciudadesFile.getStringList(i + ".ciudadanos");
                        for (String j : ciudadanos) {
                            UserManager userManager = luckPermsAPI.getUserManager();

                            OfflinePlayer player = Bukkit.getOfflinePlayer(j);
                            CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

                            userFuture.thenAcceptAsync(userI -> {
                                utils.removeSuffix(userI, suffix);
                            });
                        }
                        ciudadesFile.set(i, null);
                        ciudadesFile.save();
                    }
                    ciudadesFile.reload();
                } else {
                    for (String i : ciudadesFile.getKeys(false)) {
                        if (ciudadesFile.getString(i + ".nombre").equalsIgnoreCase(args[2])) {
                            String suffix = "&f[&b" + ciudadesFile.getString(i + ".displayname") + "&f]&r";
                            ciudadanos = ciudadesFile.getStringList(i + ".ciudadanos");
                            for (String j : ciudadanos) {
                                UserManager userManager = luckPermsAPI.getUserManager();

                                OfflinePlayer player = Bukkit.getOfflinePlayer(j);
                                CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

                                userFuture.thenAcceptAsync(userI -> {
                                    utils.removeSuffix(userI, suffix);
                                });
                            }
                            ciudadesFile.set(i, null);
                            ciudadesFile.save();
                            ciudadesFile.reload();
                        }
                    }
                }
                return true;
            } else {
                user.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFile.getString("InvalidArgument").replace("{prefix}", prefix)));
            }
        } else {
            user.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', messagesFile.getString("NoArguments").replace("{prefix}", prefix)));

        }
        return true;
    }
}
