package me.m0dii.venturacalendar.base.configutils;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.utils.Messenger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    protected final VenturaCalendar plugin;
    private final File file;
    protected FileConfiguration config;

    protected Config(File parentFile, String configName, VenturaCalendar plugin) {
        this.plugin = plugin;

        file = new File(parentFile, configName);
    }

    protected FileConfiguration loadConfig() {
        config = createConfig();

        config.options().copyHeader(true);
        config.options().copyDefaults(true);

        saveConfig();

        return config;
    }

    protected void saveConfig() {
        try {
            config.save(file);
        }
        catch (IOException ex) {
            Messenger.log(Messenger.Level.DEBUG, ex);
        }
    }

    protected FileConfiguration reloadConfig() {
        config = loadConfig();

        return config;
    }

    protected void deleteConfig() {
        if (file.exists())
            file.delete();
    }

    protected FileConfiguration createConfig() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            plugin.saveResource(file.getName(), false);
        }

        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(file);

            Messenger.log(Messenger.Level.INFO, "Successfully loaded " + file.getName() + ".");
        }
        catch (IOException | InvalidConfigurationException ex) {
            Messenger.log(Messenger.Level.WARN, "Error while loading " + file.getName() + ".");
            Messenger.log(Messenger.Level.DEBUG, ex);
        }

        return config;
    }
}
