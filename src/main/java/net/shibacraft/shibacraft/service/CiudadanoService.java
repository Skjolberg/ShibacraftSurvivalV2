package net.shibacraft.shibacraft.service;

import net.shibacraft.shibacraft.Shibacraft;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class CiudadanoService {

    private final Shibacraft plugin;

    public CiudadanoService(Shibacraft plugin) {
        this.plugin = plugin;
    }

    public void commandUsageCiudadano(Player p, YamlManager f){
        if (!f.getStringList("UsageCitizen").isEmpty()) {
            List<String> usageCommand;
            usageCommand = f.getStringList("UsageCitizen");
            for (String i : usageCommand) {
                p.sendMessage(Utils.toLegacyColors(
                        i));
            }
        }
    }

    public List<String> CitizenAbandon(String path, Player user){
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        List<String> ciudadanos = ciudadesFile.getStringList(path + ".ciudadanos");
        ciudadanos.remove(user.getName());
        return ciudadanos;
    }

    public int setRestante(String path){
        YamlManager ciudadesFile = new YamlManager(plugin, "ciudades");
        return ciudadesFile.getInt(path + ".restante") + 1;
    }

}
