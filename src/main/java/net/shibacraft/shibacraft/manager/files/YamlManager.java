package net.shibacraft.shibacraft.manager.files;

import net.shibacraft.shibacraft.Shibacraft;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class YamlManager extends YamlConfiguration {

    private final String fileName;
    private final Shibacraft plugin;
    private File file;
    private File folder;

    public YamlManager(Shibacraft plugin, String fileName, File folder) {
        this.folder = folder;
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
        createFile();

    }

    public YamlManager(Shibacraft plugin, String fileName) {
        this(plugin, fileName, plugin.getDataFolder());
    }

    private void createFile() {
        try {
            file = new File(this.folder, this.fileName);
            if (file.exists()) {
                load(file);
                save(file);
                return;
            }
            if (this.plugin.getResource(this.fileName) != null) {
                this.plugin.saveResource(this.fileName, false);
            } else {
                save(file);
            }
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Creation of Configuration '" + this.fileName + "' failed.", e);
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Save of the file '" + this.fileName + "' failed.", e);
        }
    }

    public void reload() {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Reload of the file '" + this.fileName + "' failed.", e);
        }
    }


}
