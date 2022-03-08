package net.shibacraft.shibacraft;

import net.shibacraft.shibacraft.dependencies.LuckPermsDependency;
import net.shibacraft.shibacraft.listeners.CitizenChatInvitationListener;
import net.shibacraft.shibacraft.commands.*;
import net.shibacraft.shibacraft.manager.files.YamlManager;
import net.shibacraft.shibacraft.manager.players.PlayerInvitationManager;
import net.shibacraft.shibacraft.service.CiudadanoService;
import net.shibacraft.shibacraft.service.PresidenteService;
import net.shibacraft.shibacraft.service.ShibacraftService;
import net.shibacraft.shibacraft.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Shibacraft extends JavaPlugin {

    public void onEnable() {
        PlayerInvitationManager playerManager = new PlayerInvitationManager();
        Utils utils = new Utils(this);
        PresidenteService presidenteService = new PresidenteService(this);
        CiudadanoService ciudadanoService = new CiudadanoService(this);
        LuckPermsDependency luckPermsDependency = new LuckPermsDependency();
        ShibacraftService shibacraftService = new ShibacraftService(this, luckPermsDependency);
        registerListeners(playerManager, luckPermsDependency, utils, presidenteService);
        registerFiles();
        registerConfig();
        registerCommands(playerManager, presidenteService, ciudadanoService, luckPermsDependency, shibacraftService);
        FileConfiguration config = getConfig();

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            getLogger().info("PlaceholderAPI: Habilitado");
        } else {
            getLogger().warning("PlaceholderAPI: Deshabilitado");
        }

        if (config.getBoolean("Wallet")) {
            getLogger().info(Utils.toLegacyColors("&bWallet: &atrue"));
        } else {
            getLogger().info(Utils.toLegacyColors("&bWallet: &4false"));
        }
        if (config.getBoolean("Discord")) {
            getLogger().info(Utils.toLegacyColors("&bDiscord: &atrue"));
        } else {
            getLogger().info(Utils.toLegacyColors("&bDiscord: &4false"));
        }
        if (config.getBoolean("Mapa")) {
            getLogger().info(Utils.toLegacyColors("&bMapa: &atrue"));
        } else {
            getLogger().info(Utils.toLegacyColors("&bMapa: &4false"));
        }
        if (config.getBoolean("Web")) {
            getLogger().info(Utils.toLegacyColors("&bWeb: &atrue"));
        } else {
            getLogger().info(Utils.toLegacyColors("&bWeb: &4false"));
        }
        if (config.getBoolean("Wiki")) {
            getLogger().info(Utils.toLegacyColors("&bWiki: &atrue"));
        } else {
            getLogger().info(Utils.toLegacyColors("&bWiki: &4false"));
        }
        if (config.getBoolean("Ayuda")) {
            getLogger().info(Utils.toLegacyColors("&bAyuda: &atrue"));
        } else {
            getLogger().info(Utils.toLegacyColors("&bAyuda: &4false"));
        }
        if (config.getBoolean("Presidente")) {
            getLogger().info(Utils.toLegacyColors("&bPresidente: &atrue"));
        } else {
            getLogger().info(Utils.toLegacyColors("&bPresidente: &4false"));
        }
        if (config.getBoolean("Ciudadano")) {
            getLogger().info(Utils.toLegacyColors("&bCiudadano: &atrue"));
        } else {
            getLogger().info(Utils.toLegacyColors("&bCiudadano: &4false"));
        }
    }

    public void onDisable() {

    }

    /*
     * Commands
     */
    public void registerCommands(PlayerInvitationManager playerManager, PresidenteService presidenteService, CiudadanoService ciudadanoService, LuckPermsDependency luckPermsDependency, ShibacraftService shibacraftService) {

        Objects.requireNonNull(this.getCommand("Shibacraft")).setExecutor(new ShibacraftCommand(this, shibacraftService));
        Objects.requireNonNull(this.getCommand("Shibacraft")).setPermission("shibacraft.admin");
        Objects.requireNonNull(this.getCommand("Shibacraft")).setTabCompleter(new TabCompletionShibacraft());

        if (getConfig().getBoolean("Wallet")) {
            Objects.requireNonNull(this.getCommand("Wallet")).setExecutor(new Wallet(this));
        }
        if (getConfig().getBoolean("Discord")) {
            Objects.requireNonNull(this.getCommand("Discord")).setExecutor(new Discord(this));
        }
        if (getConfig().getBoolean("Mapa")) {
            Objects.requireNonNull(this.getCommand("Mapa")).setExecutor(new Mapa(this));
        }
        if (getConfig().getBoolean("Web")) {
            Objects.requireNonNull(this.getCommand("Web")).setExecutor(new Web(this));
        }
        if (getConfig().getBoolean("Wiki")) {
            Objects.requireNonNull(this.getCommand("Wiki")).setExecutor(new Wiki(this));
        }
        if (getConfig().getBoolean("Ayuda")) {
            Objects.requireNonNull(this.getCommand("Ayuda")).setExecutor(new Ayuda(this));
        }
        if (getConfig().getBoolean("Presidente")) {
            Objects.requireNonNull(this.getCommand("Presidente")).setExecutor(new Presidente(this, playerManager, presidenteService, luckPermsDependency));
            Objects.requireNonNull(this.getCommand("Presidente")).setTabCompleter(new TabCompletionPresident());
            Objects.requireNonNull(this.getCommand("Presidente")).setPermission("shibacraft.presidente");
        }
        if (getConfig().getBoolean("Ciudadano")) {
            Objects.requireNonNull(this.getCommand("Ciudadano")).setExecutor(new Ciudadano(this, ciudadanoService, luckPermsDependency));
            Objects.requireNonNull(this.getCommand("Ciudadano")).setTabCompleter(new TabCompletionCitizen());
            Objects.requireNonNull(this.getCommand("Ciudadano")).setPermission("shibacraft.ciudadano");
        }
    }

    /*
     * Listeners
     */

    public void registerListeners(PlayerInvitationManager playerManager, LuckPermsDependency luckPermsDependency, Utils utils, PresidenteService presidenteService) {
        this.getServer().getPluginManager().registerEvents(new CitizenChatInvitationListener(this, playerManager, luckPermsDependency, utils, presidenteService), this);
    }

    /*
     * Config
     */
    public void registerConfig() {
        File config = new File(this.getDataFolder(), "config.yml");

        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }
    }

    /*
     * Files
     */
    private void registerFiles() {

        YamlManager ayuda = new YamlManager(this, "ayuda");
        new YamlManager(this, "messages");
        new YamlManager(this, "ciudades");
        new YamlManager(this, "wallet");
    }
}