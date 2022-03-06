package net.shibacraft.shibacraft.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.SuffixNode;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.regex.Pattern;

public class Utils implements Listener {
    private final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    public static final Pattern STRIP_AMPERSAND_COLORS = Pattern.compile("(?i)&[0-9A-FK-ORX]");

    public void addSuffix(User user, String suffix) {
        user.data().add(SuffixNode.builder(suffix, 1).build());
        luckPermsAPI.getUserManager().saveUser(user);
    }

    public void removeSuffix(User user, String suffix) {
        user.data().remove(SuffixNode.builder(suffix, 1).build());
        luckPermsAPI.getUserManager().saveUser(user);
    }

    public void commandUsagePresidente(Player p, YamlManager f) {
        List<String> usageCommand;
        usageCommand = f.getStringList("UsagePresident");
        if (!usageCommand.isEmpty()) {
            for (String i : usageCommand) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        i));
            }
        }
    }

    public void commandUsageCiudadano(Player p, YamlManager f){
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
