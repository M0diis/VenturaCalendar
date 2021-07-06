package me.M0dii.VenturaCalendar.Game.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Base.ConfigUitls.Config;
import me.M0dii.VenturaCalendar.Base.ConfigUitls.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;

public class CommandConfig extends Config implements ConfigUtils
{
	FileConfiguration config;
	
	public CommandConfig()
	{
		super(VenturaCalendar.instance.getDataFolder(), "CommandConfig.yml");
		
		config = super.loadConfig();
	}
	
	public HashMap<Messages, String> getMessages()
	{
		HashMap<Messages, String> messages = new HashMap<>();
		
		String path = "messages.";
		
		messages.put(Messages.NO_PERMISSION, VenturaCalendar.PREFIX + getString(path + "no-permission"));
		messages.put(Messages.NOT_PLAYER, VenturaCalendar.PREFIX + getString(path + "player-only"));
		messages.put(Messages.UNKNOWN_COMMAND, VenturaCalendar.PREFIX + getString(path + "unknown-command"));
		messages.put(Messages.UNKNOWN_TIMESYSTEM, VenturaCalendar.PREFIX + getString(path + "unknown-timesystem"));
		messages.put(Messages.CONFIG_RELOADED, VenturaCalendar.PREFIX + getString(path + "config-reloaded"));
		
		return messages;
	}
	
	public FileConfiguration reloadConfig() {
		return config = super.reloadConfig();
	}
	
	@Override
	public String getString(String path)
	{
		return ChatColor.translateAlternateColorCodes('&', config.getString(path));
	}

	@Override
	public Integer getInteger(String path)
	{
		return config.getInt(path);
	}
	
	@Override
	public Long getLong(String path)
	{
		return Long.valueOf(config.getString(path, "0"));
	}
	
	@Override
	public Boolean getBoolean(String path)
	{
		return config.getBoolean(path);
	}
	
	@Override
	public List<String> getListString(String path)
	{
		if (config.getList(path) != null)
		{
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) config.getList(path);
		
			if(list != null)
				for(int index = 0; index < list.size(); index++)
					list.set(index, ChatColor.translateAlternateColorCodes('&', list.get(index)));
			
			return list;
		}
		
		return null;
	}
	
	@Override
	public ArrayList<String> getArrayListString(String path)
	{
		List<String> list = getListString(path);
		
		return new ArrayList<>(list);
	}

}
