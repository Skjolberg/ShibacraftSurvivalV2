package net.shibacraft.shibacraft.utils;

import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

public class Utils {

    private static Shibacraft plugin;
    public Utils(Shibacraft plugin) {
        Utils.plugin = plugin;
    }

    public static final Pattern STRIP_AMPERSAND_COLORS = Pattern.compile("(?i)&[0-9A-FK-ORX]");


    public static String getPrefixMessages(){
        YamlManager messagesFile = new YamlManager(plugin, "messages");
        return messagesFile.getString("Prefix");
    }

    public static String getSuffix(String presidente){

        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        return "&f[&b" + ciudadesFile.getString(presidente + ".displayname") + "&f]&r";
    }

    public static String toLegacyColors(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
