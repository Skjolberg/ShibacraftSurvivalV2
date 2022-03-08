package net.shibacraft.shibacraft.commands;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.dependencies.LuckPermsDependency;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.service.CiudadanoService;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ciudadano implements CommandExecutor {
    private final Shibacraft plugin;
    private final net.luckperms.api.LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final CiudadanoService ciudadanoService;
    private final LuckPermsDependency luckPermsDependency;

    public Ciudadano(Shibacraft plugin, CiudadanoService ciudadanoService, LuckPermsDependency luckPermsDependency) {

        this.plugin = plugin;
        this.ciudadanoService = ciudadanoService;
        this.luckPermsDependency = luckPermsDependency;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command ciudadano, String label, String[] args) {

        YamlManager messagesFile = new YamlManager(plugin, "messages");
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");

        if (!(sender instanceof Player user)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    messagesFile.getString("ConsoleSender").replace("{prefix}", Utils.getPrefixMessages())));
        } else {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("abandon")) {
                    User userLP = luckPermsAPI.getPlayerAdapter(Player.class).getUser(user);
                    for (String i : ciudadesFile.getKeys(false)) {
                        if (ciudadesFile.getStringList(i + ".ciudadanos").contains(user.getName())) {
                            ciudadesFile.set(i + ".ciudadanos", ciudadanoService.CitizenAbandon(i, user));
                            ciudadesFile.set(i + ".restante", ciudadanoService.setRestante(i));
                            ciudadesFile.save();
                            luckPermsDependency.removeSuffix(userLP, Utils.getSuffix(user.getName()));
                            if (messagesFile.getString("CitizenAbandon").length() > 0) {
                                user.sendMessage(Utils.toLegacyColors(messagesFile.getString("CitizenAbandon").replace("{city}", ciudadesFile.getString(i + ".nombre")).replace("{prefix}", Utils.getPrefixMessages())));
                            }
                            return true;
                        }
                    }
                    if (messagesFile.getString("UserNoCitizen").length() > 0) {
                        user.sendMessage(Utils.toLegacyColors(
                                messagesFile.getString("UserNoCitizen").replace("{prefix}", Utils.getPrefixMessages())));
                    }
                } else {
                    ciudadanoService.commandUsageCiudadano(user, messagesFile);
                }
            } else {
                ciudadanoService.commandUsageCiudadano(user, messagesFile);
            }
        }
        return true;
    }
}