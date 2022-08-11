package me.m0dii.venturacalendar.base.configutils;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.Month;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.base.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TimeConfig extends Config implements ConfigUtils
{
	FileConfiguration cfg;
	
	public TimeConfig(VenturaCalendar plugin)
	{
		super(plugin.getDataFolder(), "TimeConfig.yml", plugin);
		
		cfg = super.loadConfig();
		
		reload();
	}
	
	public void set(@NotNull String path, @Nullable Object obj)
	{
		cfg.set(path, obj);
		
		saveConfig();
	}
	
	final HashMap<String, TimeSystem> timeSystems = new HashMap<>();
	
	public TimeSystem getTimeSystem()
	{
		if(timeSystems.containsKey("main-time-system"))
		{
			return timeSystems.get("main-time-system");
		}
		else
		{
			Messenger.log(Messenger.Level.ERROR, "Timesystem 'main-time-system' was not found in the config.");
		}
		
		reload();
		
		return timeSystems.get("main-time-system");
	}
	
	private void reload()
	{
		timeSystems.put("main-time-system", loadTimeSystem());
	}

	private TimeSystem loadTimeSystem()
	{
		String path = "main-time-system.";
		String worldName = getString(path + "world-name");
		
		String timeZone = getString(path + "real-time.time-zone");
		boolean useTimeZone = getBoolean(path + "real-time.use-time-zone");
		boolean realTime = getBoolean(path + "real-time.enabled");
		
		// Tick
		long tickZero	= 0;
		
		// Second
		long ticksPerSecond = getLong(path + "ticks-per-second");
		long secondZero	= 0;
		
		// Minute
		long secondsPerMinute = getLong(path + "seconds-per-minute");
		long minuteZero = 0;
		
		// Hour
		long minutesPerHour = getLong(path + "minutes-per-hour");
		long hourZero = 1;
		
		// Day
		long hoursPerDay = getLong(path + "hours-per-day");
		long dayZero = getLong(path + "day-offset");
		
		List<String> dayNames = getListString(path + "days");
		
		// Week
		long daysPerWeek = getLong(path + "days-per-week");
		long weekZero = getLong(path + "week-offset");
		
		// Month
		List<Month> months = new ArrayList<>();
		List<Long> monthDays = new ArrayList<>();
		
		for(String month : getListString(path + "months"))
		{
			String[] split = month.split(", ");
			
			String monthName = split[0].trim();
			long monthDaysCount = Long.parseLong(split[1].trim());
			String seasonName = split[2].trim();
			
			Month m = new Month(monthName, monthDaysCount, seasonName);
			
			monthDays.add(monthDaysCount);
			
			months.add(m);
		}
		
		long monthZero = getLong(path + "month-offset");
		
		// Year
		long monthsPerYear = getLong(path + "months-per-year");
		long yearZero = getLong(path + "starting-year");
		
		// Era
		
		List<String> eraNames = new ArrayList<>();
		List<Long> erasBegin = new ArrayList<>();
		List<Long> erasEnd = new ArrayList<>();
		
		for(String era : getListString(path + "eras"))
		{
			String[] split = era.split(", ");
			
			String eraName = split[0].trim();
			long eraBegin = Long.parseLong(split[1].trim());
			long eraEnd =  Long.parseLong(split[2].trim());
			
			eraNames.add(eraName);
			erasBegin.add(eraBegin);
			erasEnd.add(eraEnd);
		}
		
		long eraZero = 1;
		
		TimeSystem ts = new TimeSystem(
			   worldName, "main-time-system",
			   ticksPerSecond,
			   secondsPerMinute,
			   minutesPerHour,
			   hoursPerDay,
			   daysPerWeek,
			   monthDays,
			   monthsPerYear,
			   erasBegin,
			   erasEnd,
			   
			   tickZero,
			   secondZero,
			   minuteZero,
			   hourZero,
			   dayZero,
			   weekZero,
			   monthZero,
			   yearZero,
			   eraZero,
			   
			   dayNames,
			   months,
			   eraNames
		);

		ts.setTimeZone(timeZone);
		ts.setUseTimeZone(useTimeZone);
		ts.setRealTime(realTime);
		
		return ts;
	}

	public FileConfiguration reloadConfig()
	{
		cfg = super.reloadConfig();
		
		reload();
		
		return cfg;
	}

	private ArrayList<Object> getSection(String path, String value)
	{
		ArrayList<Object> names = new ArrayList<>();
		
		ConfigurationSection section = cfg.getConfigurationSection(path);
		
		if (section != null)
			for(String key : section.getKeys(false))
				names.add(getString(path + "." + key + "." + value));

		return names;		
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
