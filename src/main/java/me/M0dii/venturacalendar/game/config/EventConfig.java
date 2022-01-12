package me.M0dii.venturacalendar.game.config;

import me.M0dii.venturacalendar.VenturaCalendar;
import me.M0dii.venturacalendar.base.configutils.Config;
import me.M0dii.venturacalendar.base.configutils.ConfigUtils;
import me.M0dii.venturacalendar.base.dateutils.FromTo;
import me.M0dii.venturacalendar.base.dateutils.MonthEvent;
import me.M0dii.venturacalendar.base.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventConfig extends Config implements ConfigUtils
{
	FileConfiguration cfg;
	
	public EventConfig(VenturaCalendar plugin)
	{
		super(plugin.getDataFolder(), "Events.yml", plugin);
		
		cfg = super.loadConfig();
		
		VenturaCalendar.PREFIX = getString("messages.prefix");
	}
	
	public List<MonthEvent> getEvents()
	{
		ConfigurationSection sec = cfg.getConfigurationSection("events");
		
		List<MonthEvent> events = new ArrayList<>();
		
		if(sec != null)
		{
			sec.getValues(false).forEach((k, v) ->
			{
				ConfigurationSection eventSection = sec.getConfigurationSection(k);
				
				if(eventSection != null)
				{
					String eventName = Utils.format(eventSection.getString("name"));
					
					FromTo fromTo = new FromTo(eventSection.getInt("days.from"), eventSection.getInt("days.to"));
					
					List<String> description = eventSection.getStringList("description").stream()
							.map(Utils::format)
							.collect(Collectors.toList());
					
					Material m = Material.getMaterial(eventSection.getString("material", "RED_STAINED_GLASS_PANE"));
					
					String month = eventSection.getString("month");
					
					List<String> commands = eventSection.getStringList("commands");
					
					MonthEvent event = new MonthEvent(eventName, month, m, fromTo, description, commands);
					
					events.add(event);
				}
			});
		}
		
		return events;
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
