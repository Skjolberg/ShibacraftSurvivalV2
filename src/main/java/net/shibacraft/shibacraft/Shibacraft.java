package net.shibacraft.shibacraft;

import net.shibacraft.shibacraft.listeners.CitizenChatInvitationListener;
import net.shibacraft.shibacraft.commands.*;
import net.shibacraft.shibacraft.fileManager.FileManager;
import net.shibacraft.shibacraft.playerManager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Shibacraft extends JavaPlugin {

    PluginDescriptionFile pdf = getDescription();
    public String version = ChatColor.GREEN + pdf.getVersion() + ChatColor.WHITE;
    public String nombre = ChatColor.LIGHT_PURPLE + getName() + ChatColor.WHITE;

    public void onEnable() {
        PlayerManager playerManager = new PlayerManager();

        registerListeners(playerManager);
        registerFiles();
        registerConfig();
        registerCommands(playerManager);
        FileConfiguration config = getConfig();

        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.WHITE + "[" + nombre + "]" + " is now enabled (version: " + version + ")");
        if (config.getBoolean("Wallet")) {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bWallet: &atrue"));
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bWallet: &4false"));
        }
        if (config.getBoolean("Discord")) {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bDiscord: &atrue"));
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bDiscord: &4false"));
        }
        if (config.getBoolean("Mapa")) {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bMapa: &atrue"));
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bMapa: &4false"));
        }
        if (config.getBoolean("Web")) {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bWeb: &atrue"));
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bWeb: &4false"));
        }
        if (config.getBoolean("Wiki")) {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bWiki: &atrue"));
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bWiki: &4false"));
        }
        if (config.getBoolean("Ayuda")) {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bAyuda: &atrue"));
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bAyuda: &4false"));
        }
        if (config.getBoolean("Presidente")) {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bPresidente: &atrue"));
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bPresidente: &4false"));
        }
        if (config.getBoolean("Ciudadano")) {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bCiudadano: &atrue"));
        } else {
            Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.WHITE + "[" + nombre + "]" + " &bCiudadano: &4false"));
        }
    }

    public void onDisable() {

    }

    /*
     * Commands
     */
    public void registerCommands(PlayerManager playerManager) {
        Objects.requireNonNull(this.getCommand("Shibacraft")).setExecutor(new ShibacraftCommand(this));
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
            Objects.requireNonNull(this.getCommand("Presidente")).setExecutor(new Presidente(this, playerManager));
            Objects.requireNonNull(this.getCommand("Presidente")).setTabCompleter(new TabCompletionPresident());
            Objects.requireNonNull(this.getCommand("Presidente")).setPermission("shibacraft.presidente");
        }
        if (getConfig().getBoolean("Ciudadano")) {
            Objects.requireNonNull(this.getCommand("Ciudadano")).setExecutor(new Ciudadano(this));
            Objects.requireNonNull(this.getCommand("Ciudadano")).setTabCompleter(new TabCompletionCitizen());
        }
    }

    /*
     * Listeners
     */

    public void registerListeners(PlayerManager playerManager){
        this.getServer().getPluginManager().registerEvents(new CitizenChatInvitationListener(this, playerManager), this);
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

        new FileManager(this, "ayuda");
        new FileManager(this, "messages");
        new FileManager(this, "ciudades");
        new FileManager(this, "wallet");
    }
}