package net.shibacraft.shibacraft.service;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.dependencies.LuckPermsDependency;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ShibacraftService {

    private final Shibacraft plugin;
    private final net.luckperms.api.LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final LuckPermsDependency luckPermsDependency;

    public ShibacraftService(Shibacraft plugin, LuckPermsDependency luckPermsDependency) {
        this.plugin = plugin;
        this.luckPermsDependency = luckPermsDependency;
    }

    public void deleteCitys(CommandSender sender){
        YamlManager messagesFile = new YamlManager(plugin, "messages");
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        List<String> ciudadanos;
        for (String i : ciudadesFile.getKeys(false)) {
            String suffix = Utils.getSuffix(i);
            ciudadanos = ciudadesFile.getStringList(i + ".ciudadanos");
            for (String j : ciudadanos) {
                UserManager userManager = luckPermsAPI.getUserManager();

                OfflinePlayer p = Bukkit.getOfflinePlayer(j);
                CompletableFuture<User> userFuture = userManager.loadUser(p.getUniqueId());

                userFuture.thenAcceptAsync(userI -> {
                    luckPermsDependency.removeSuffix(userI, suffix);
                });
            }
            ciudadesFile.set(i, null);
            ciudadesFile.save();
        }
        sender.sendMessage(Utils.toLegacyColors(messagesFile.getString("CityDeleteAll").replace("{prefix}", Utils.getPrefixMessages())));
    }

    public void deleteCity(CommandSender sender, String arg){
        YamlManager messagesFile = new YamlManager(plugin, "messages");
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        List<String> ciudadanos;
        for (String i : ciudadesFile.getKeys(false)) {
            if (ciudadesFile.getString(i + ".nombre").equalsIgnoreCase(arg)) {
                String suffix = Utils.getSuffix(i);
                ciudadanos = ciudadesFile.getStringList(i + ".ciudadanos");
                for (String j : ciudadanos) {
                    UserManager userManager = luckPermsAPI.getUserManager();

                    OfflinePlayer p = Bukkit.getOfflinePlayer(j);
                    CompletableFuture<User> userFuture = userManager.loadUser(p.getUniqueId());

                    userFuture.thenAcceptAsync(userI -> {
                        luckPermsDependency.removeSuffix(userI, suffix);
                    });
                }
                ciudadesFile.set(i, null);
                ciudadesFile.save();
                sender.sendMessage(Utils.toLegacyColors(messagesFile.getString("CitySuccessfullyEliminated").replace("{prefix}", Utils.getPrefixMessages())));
                return;
            }
        }
        sender.sendMessage(Utils.toLegacyColors(messagesFile.getString("CityNotExist").replace("{prefix}", Utils.getPrefixMessages())));
    }






}
