package me.M0dii.VenturaCalendar.Game.Config;

import java.util.HashMap;
import java.util.List;

import me.M0dii.VenturaCalendar.Base.DateUtils.FromTo;
import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Base.ConfigUitls.Config;
import me.M0dii.VenturaCalendar.Base.ConfigUitls.ConfigUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;

public class BaseConfig extends Config implements ConfigUtils
{
	FileConfiguration cfg;
	
	HashMap<String, FromTo> redeemableMonths = new HashMap<>();
	HashMap<Messages, String> messages = new HashMap<>();
	
	public boolean newDayMessageEnabled()
	{
		return getBoolean("new-day.message.enabled");
	}
	
	public boolean rewardsEnabled()
	{
		return getBoolean("rewards.enabled");
	}
	
	public boolean titleEnabled()
	{
		return getBoolean("new-day.title.enabled");
	}
	
	public BaseConfig(VenturaCalendar plugin)
	{
		super(plugin.getDataFolder(), "BaseConfig.yml");
		
		cfg = super.loadConfig();
		
		VenturaCalendar.PREFIX = getString("messages.prefix");
	}
	
	private String getNewDayMessage()
	{
		StringBuilder msg = new StringBuilder();
		
		for(String m : getListString("new-day.message.text"))
			msg.append(Utils.format(m)).append("\n");
		
		return msg.toString();
	}
	
	public boolean redeemWhitelistEnabled()
	{
		return cfg.getBoolean("rewards.redeemable-months.enabled");
	}
	
	public HashMap<String, FromTo> getRedeemableMonths()
	{
		ConfigurationSection sec = cfg.
				getConfigurationSection("rewards.redeemable-months");
		
		if(sec != null)
		{
			sec.getValues(false).forEach((k, v) -> {
			
				if(!k.equalsIgnoreCase("enabled"))
				{
					String[] fromToString = String.valueOf(v).split("-");
					
					int from = Integer.parseInt(fromToString[0]);
					int to = Integer.parseInt(fromToString[1]);
					
					redeemableMonths.put(k, new FromTo(from, to));
				}
			});
		}

		return redeemableMonths;
	}
	
	public String getMessage(Messages msg)
	{
		return this.getMessages().get(msg);
	}
	
	private HashMap<Messages, String> getMessages()
	{
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
		cfg = super.reloadConfig();
		
		return cfg;
	}
	
	@Override
	public String getString(String path)
	{
		return ChatColor.translateAlternateColorCodes('&', cfg.getString(path));
	}

	@Override
	public Integer getInteger(String path)
	{
		return cfg.getInt(path);
	}
	
	@Override
	public Long getLong(String path)
	{
		return Long.valueOf(cfg.getString(path, "0"));
	}
	
	@Override
	public Boolean getBoolean(String path)
	{
		return cfg.getBoolean(path);
	}
	
	@Override
	public List<String> getListString(String path)
	{
		if (cfg.getList(path) != null)
		{
			List<String> list = cfg.getStringList(path);
			
			for(int i = 0; i < list.size(); i++)
				list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
			
			return list;
		}
		
		return null;
	}
}
