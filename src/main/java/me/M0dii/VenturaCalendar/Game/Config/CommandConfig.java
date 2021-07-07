package me.M0dii.VenturaCalendar.Game.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.M0dii.VenturaCalendar.Base.Utils.MsgUtils;
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
		super(VenturaCalendar.instance.getDataFolder(), "Config.yml");
		
		config = super.loadConfig();
	}
	
	public HashMap<Messages, String> getNewDayMessages()
	{
		HashMap<Messages, String> messages = new HashMap<>();
		
		String path = "new-day.";
		
		StringBuilder msg = new StringBuilder();
		
		for(String m : getListString(path + "message"))
			msg.append(MsgUtils.format(m)).append(" ");
		
		messages.put(Messages.NEW_DAY, msg.toString());
		
		return messages;
	}
	
	public HashMap<Messages, String> getMessages()
	{
		HashMap<Messages, String> messages = new HashMap<>();
		
		String path = "messages.";
		
		messages.put(Messages.NO_PERMISSION, VenturaCalendar.PREFIX +
				getString(path + "no-permission"));
		messages.put(Messages.NOT_PLAYER, VenturaCalendar.PREFIX +
				getString(path + "player-only"));
		messages.put(Messages.UNKNOWN_COMMAND, VenturaCalendar.PREFIX +
				getString(path + "unknown-command"));
		messages.put(Messages.UNKNOWN_TIMESYSTEM, VenturaCalendar.PREFIX +
				getString(path + "unknown-timesystem"));
		messages.put(Messages.CONFIG_RELOADED, VenturaCalendar.PREFIX +
				getString(path + "config-reloaded"));
		
		return messages;
	}
	
	public FileConfiguration reloadConfig()
	{
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
			List<String> list = config.getStringList(path);
			
			for(int i = 0; i < list.size(); i++)
				list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
			
			return list;
		}
		
		return null;
	}
}
