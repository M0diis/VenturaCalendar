package me.M0dii.venturacalendar.base.configutils;

import java.io.File;
import java.io.IOException;

import me.M0dii.venturacalendar.VenturaCalendar;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config
{
	final VenturaCalendar plugin;
	
	final File file;
	FileConfiguration config;
	
	protected Config(File parentFile, String configName, VenturaCalendar plugin)
	{
		this.plugin = plugin;
		
		file = new File(parentFile, configName);
	}

	protected FileConfiguration loadConfig()
	{
		config = createConfig();

		config.options().copyHeader(true);
		config.options().copyDefaults(true);
		
		saveConfig();
		
		return config;
	}

	protected void saveConfig()
	{
		try
		{
			config.save(file);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	protected FileConfiguration reloadConfig()
	{
		config = loadConfig();
		
		return config;
	}
	
	protected void deleteConfig()
	{
		if(file.exists())
			file.delete();
	}
	
	protected FileConfiguration createConfig()
	{
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			
			plugin.saveResource(file.getName(), false);
		}

		FileConfiguration config = new YamlConfiguration();
		
		try
		{
			config.load(file);
			
			plugin.getLogger().info("Succsessfully loaded " + file.getName() + "!");
		}
		catch (IOException | InvalidConfigurationException ex)
		{
			plugin.getLogger().warning("Error while loading " + file.getName() + "!");
			
			ex.printStackTrace();
		}

		return config;
	}
}