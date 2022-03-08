package net.shibacraft.shibacraft.service;

import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class PresidenteService {

    private final Shibacraft plugin;

    public PresidenteService(Shibacraft plugin) {
        this.plugin = plugin;
    }



    public void commandUsagePresidente(Player p, YamlManager f) {
        List<String> usageCommand;
        usageCommand = f.getStringList("UsagePresident");
        if (!usageCommand.isEmpty()) {
            for (String i : usageCommand) {
                p.sendMessage(Utils.toLegacyColors(
                        i));
            }
        }
    }

    public List<String> getCitizensList(Player user){
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        return ciudadesFile.getStringList(user.getName() + ".ciudadanos");
    }

    public void viewCitizens(Player user){
        YamlManager messagesFile = new YamlManager(plugin, "messages");

        if (messagesFile.getString("CitizenListHeader").length() > 0) {
            user.sendMessage(Utils.toLegacyColors(
                    messagesFile.getString("CitizenListHeader").replace("{prefix}", Utils.getPrefixMessages())));
            user.sendMessage("");
        }
        int cont = 1;
        for (String i : getCitizensList(user)) {
            user.sendMessage(Utils.toLegacyColors( "&e" + cont + ".&f " + i));
            cont++;
        }
        if (messagesFile.getString("CitizenListFooter").length() > 0) {
            user.sendMessage("");
            user.sendMessage(Utils.toLegacyColors(
                    messagesFile.getString("CitizenListFooter").replace("{prefix}", Utils.getPrefixMessages())));
        }
    }

    public List<String> removeCitizen(Player presidente, String user){
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        List<String> ciudadanos = ciudadesFile.getStringList(presidente.getName() + ".ciudadanos");
        ciudadanos.remove(user);
        return ciudadanos;
    }

    public List<String> addCitizen(Player presidente, String user){
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        List<String> ciudadanos = ciudadesFile.getStringList(presidente.getName() + ".ciudadanos");
        ciudadanos.add(user);
        return ciudadanos;
    }

}
