package me.M0dii.VenturaCalendar.Game.Config;

import java.util.HashMap;
import java.util.List;

import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Base.ConfigUitls.Config;
import me.M0dii.VenturaCalendar.Base.ConfigUitls.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;

public class CommandConfig extends Config implements ConfigUtils
{
	FileConfiguration config;
	
	private boolean newDayMessageEnabled; 
	private boolean rewardsEnabled; 
	private boolean titleEnabled; 
	
	public boolean newDayMessageEnabled()
	{
		return newDayMessageEnabled;
	}
	
	public boolean rewardsEnabled()
	{
		return rewardsEnabled;
	}
	
	public boolean titleEnabled()
	{
		return titleEnabled;
	}
	
	private void load()
	{
		this.newDayMessageEnabled = getBoolean("new-day.message.enabled");
		this.rewardsEnabled = getBoolean("rewards.enabled");
		this.rewardsEnabled = getBoolean("new-day.title.enabled");
	}
	
	public CommandConfig(VenturaCalendar plugin)
	{
		super(plugin.getDataFolder(), "BaseConfig.yml");
		
		config = super.loadConfig();
	}
	
	private String getNewDayMessage()
	{
		StringBuilder msg = new StringBuilder();
		
		for(String m : getListString("new-day.message.text"))
			msg.append(Utils.format(m)).append("\n");
		
		return msg.toString();
	}
	
	public String getMessage(Messages msg)
	{
		return this.getMessages().get(msg);
	}
	
	private HashMap<Messages, String> getMessages()
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
		messages.put(Messages.REDEEMED, VenturaCalendar.PREFIX +
				getString(path + "redeemed"));
		
		messages.put(Messages.TITLE_TEXT, getString("new-day.title.text"));
		messages.put(Messages.SUBTITLE_TEXT, getString("new-day.title.subtitle"));
		messages.put(Messages.NEW_DAY_TEXT, getNewDayMessage());
		
		return messages;
	}
	
	public FileConfiguration reloadConfig()
	{
		config = super.reloadConfig();
		
		load();
		
		return config;
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
