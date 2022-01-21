package me.m0dii.venturacalendar.game.config;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.configutils.Config;
import me.m0dii.venturacalendar.base.configutils.ConfigUtils;
import me.m0dii.venturacalendar.base.dateutils.FromTo;
import me.m0dii.venturacalendar.base.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseConfig extends Config implements ConfigUtils
{
	FileConfiguration cfg;
	
	final HashMap<String, FromTo> redeemableMonths = new HashMap<>();
	final HashMap<Messages, String> messages = new HashMap<>();
	
	public boolean debug()
	{
		VenturaCalendar.debug = getBoolean("debug");
		
		return VenturaCalendar.debug;
	}
	
	public boolean updateCheck()
	{
		return getBoolean("update-check");
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
		super(plugin.getDataFolder(), "BaseConfig.yml", plugin);
		
		cfg = super.loadConfig();
		
		VenturaCalendar.PREFIX = getString("messages.prefix");
		
		debug();
	}
	
	public List<String> getNewDayCommands()
	{
		return getListString("new-day.commands");
	}
	
	public Optional<String> getNewDayMessage()
	{
		List<String> msg = getListString("new-day.messages");
		
		if(msg.size() == 0)
		{
			return Optional.empty();
		}
		
		boolean allEmpty = msg.stream().allMatch(String::isEmpty);
		
		if(allEmpty)
		{
			return Optional.empty();
		}
		
		return Optional.of(msg.stream()
				.map(m -> Utils.format(m) + "\n")
				.collect(Collectors.joining()).trim());
	}
	
	public Optional<String> getActionBarMessage()
	{
		if(!getBoolean("action-bar.enabled"))
			return Optional.empty();
		
		return Optional.of(getString("action-bar.text"));
	}
	
	public boolean redeemWhitelistEnabled()
	{
		return cfg.getBoolean("rewards.redeemable-months.enabled");
	}
	
	public HashMap<String, FromTo> getRedeemableMonths()
	{
		ConfigurationSection sec = cfg.getConfigurationSection("rewards.redeemable-months");
		
		if(sec != null)
		{
			sec.getValues(false).forEach((k, v) -> {
			
				if(!k.equalsIgnoreCase("enabled"))
					redeemableMonths.put(k, new FromTo(String.valueOf(v)));
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
		messages.put(Messages.CONFIG_RELOADED, VenturaCalendar.PREFIX +
				getString(path + "config-reloaded"));
		messages.put(Messages.REDEEMED, VenturaCalendar.PREFIX +
				getString(path + "redeemed"));
		
		messages.put(Messages.TITLE_TEXT, getString("new-day.title.text"));
		messages.put(Messages.SUBTITLE_TEXT, getString("new-day.title.subtitle"));
		
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
		if(path == null)
			return "";
		
		String str = cfg.getString(path);
		
		if(str == null || str.isEmpty())
			return "";
		
		return Utils.format(str);
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
		return cfg.getStringList(path).stream()
				.map(Utils::format)
				.collect(Collectors.toList());
	}
}
