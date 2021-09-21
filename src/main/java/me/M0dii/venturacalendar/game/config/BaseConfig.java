package me.M0dii.venturacalendar.game.config;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import me.M0dii.venturacalendar.base.dateutils.FromTo;
import me.M0dii.venturacalendar.base.utils.Utils;
import me.M0dii.venturacalendar.VenturaCalendar;
import me.M0dii.venturacalendar.base.configutils.Config;
import me.M0dii.venturacalendar.base.configutils.ConfigUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;

public class BaseConfig extends Config implements ConfigUtils
{
	FileConfiguration cfg;
	
	final HashMap<String, FromTo> redeemableMonths = new HashMap<>();
	final HashMap<Messages, String> messages = new HashMap<>();
	
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
		super(plugin.getDataFolder(), "BaseConfig.yml", plugin);
		
		cfg = super.loadConfig();
		
		VenturaCalendar.PREFIX = getString("messages.prefix");
	}
	
	private String getNewDayMessage()
	{
		return getListString("new-day.message.text").stream()
				.map(m -> Utils.format(m) + "\n")
				.collect(Collectors.joining()).trim();
	}
	
	public Optional<String> actionBar()
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
		return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(cfg.getString(path)));
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
			return cfg.getStringList(path).stream().map(Utils::format)
					.collect(Collectors.toList());
		
		return null;
	}
}
