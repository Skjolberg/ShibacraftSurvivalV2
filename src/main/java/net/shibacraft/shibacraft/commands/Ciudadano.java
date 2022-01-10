package net.shibacraft.shibacraft.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.SuffixNode;
import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.fileManager.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class Ciudadano implements CommandExecutor, Listener {
    private Shibacraft plugin;
    private LuckPerms luckPermsAPI = LuckPermsProvider.get();

    public Ciudadano(Shibacraft plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command ciudadano, String label, String[] args) {

        Player user = (Player) sender;
        FileManager messagesFile = new FileManager(plugin, "messages");
        FileManager ciudadesFile = new FileManager(plugin, "ciudades");
        final String prefix = messagesFile.getString("Prefix");

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("abandon")) {
                User userLP = luckPermsAPI.getPlayerAdapter(Player.class).getUser(user);
                for (String i : ciudadesFile.getKeys(false)) {
                    List<String> ciudadanos = ciudadesFile.getStringList(i + ".ciudadanos");
                    for (String j : ciudadanos) {
                        if (user.getName().equals(j)) {
                            ciudadanos.remove(ciudadanos.indexOf(j));
                            ciudadesFile.set(i + ".ciudadanos", ciudadanos);
                            ciudadesFile.set(user.getName() + ".restante", ciudadesFile.getInt(i + ".restante") + 1);
                            ciudadesFile.save();
                            ciudadesFile.reload();
                            String suffix = "&f[&b" + ciudadesFile.getString(i + ".nombre") + "&f]&r";
                            removeSuffix(userLP, suffix);
                            if (messagesFile.getString("CitizenAbandon").length() > 0) {
                                user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        messagesFile.getString("CitizenAbandon").replace("{city}", ciudadesFile.getString(i + ".nombre")).replace("{prefix}", prefix)));
                            }
                            return true;
                        }
                    }
                }
                if (messagesFile.getString("UserNoCitizen").length() > 0) {
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            messagesFile.getString("UserNoCitizen").replace("{prefix}", prefix)));
                }
            }
            else {
                commandUsage(user, messagesFile);
            }
        } else {
            commandUsage(user, messagesFile);
        }
        return true;
    }

    public void removeSuffix(User user, String suffix) {
        user.data().remove(SuffixNode.builder(suffix, 1).build());
        luckPermsAPI.getUserManager().saveUser(user);
    }
    public void commandUsage(Player p, FileManager f){
        if (!f.getStringList("UsageCitizen").isEmpty()) {
            List<String> usageCommand;
            usageCommand = f.getStringList("UsageCitizen");
            for (String i : usageCommand) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        i));
            }
        }
    }
}