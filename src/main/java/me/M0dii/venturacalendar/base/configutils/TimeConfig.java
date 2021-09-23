package me.M0dii.venturacalendar.base.configutils;

import me.M0dii.venturacalendar.VenturaCalendar;
import me.M0dii.venturacalendar.base.dateutils.MonthEvent;
import me.M0dii.venturacalendar.base.dateutils.TimeSystem;
import me.M0dii.venturacalendar.base.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
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
	
	private void reload()
	{
		ConfigurationSection timeSystemsName = cfg.getConfigurationSection("Time-Systems");
		
		if(timeSystemsName != null)
			for(String timeSystemName : timeSystemsName.getKeys(false))
				timeSystems.put(timeSystemName, getTimeSystem(timeSystemName));
	}
	
	public HashMap<String, TimeSystem> getTimeSystems()
	{
		return timeSystems;
	}
	
	private TimeSystem getTimeSystem(String timeSystemName)
	{
		String defaultPath = "Time-Systems." + timeSystemName + ".";
		String worldname = getString(defaultPath + "world-name");
		String path;
		
		// Tick
		long tickZero	= 0;
		
		// Second
		long ticksPerSecond = getLong(defaultPath + "second.ticksPerSecond");
		long secondZero	= 0;
		
		// Minute
		long secondsPerMinute = getLong(defaultPath + "minute.secondsPerMinute");
		long minuteZero = 0;
		
		// Hour
		long minutesPerHour = getLong(defaultPath + "hour.minutesPerHour");
		long hourZero = 1;
		
		// Day
		path = defaultPath + "day.";
		
		long hoursPerDay = getLong(path + "hoursPerDay");
		long dayZero = 1;
		
		ArrayList<String> dayNames = getSection(path + "dayNames", "name").stream()
				.map(dayNameObject -> (String)dayNameObject)
				.collect(Collectors.toCollection(ArrayList::new));
		
		// Week
		long daysPerWeek = getLong(defaultPath + "week.daysPerWeek");
		long weekZero = 1;
		
		// Month
		path = defaultPath + "month.daysPerMonth";
		
		ArrayList<Long> daysPerMonth = getSection(path, "days").stream()
				.map(daysThisMonthObject -> Long.valueOf((String)daysThisMonthObject))
				.collect(Collectors.toCollection(ArrayList::new));
		
		long monthZero = 1;
		
		ArrayList<String> monthNames = getSection(path, "name").stream()
				.map(monthNameObject -> (String)monthNameObject)
				.collect(Collectors.toCollection(ArrayList::new));
		
		ArrayList<String> seasonNames = getSection(path, "season").stream()
				.map(seasonNameObject -> (String)seasonNameObject)
				.collect(Collectors.toCollection(ArrayList::new));
		
		// Year
		path = defaultPath + "year.";
		
		long monthsPerYear = getLong(path + "monthsPerYear");
		long yearZero = getZero(path);
		
		// Era
		path = defaultPath + "era.";
		
		ArrayList<Long> erasBegin = getSection(path + "eras", "startYear").stream()
				.map(erasBeginObject -> Long.valueOf((String)erasBeginObject))
				.collect(Collectors.toCollection(ArrayList::new));
		
		ArrayList<Long> erasEnd = getSection(path + "eras", "endYear").stream()
				.map(erasEndObject -> Long.valueOf((String)erasEndObject))
				.collect(Collectors.toCollection(ArrayList::new));
		
		ArrayList<String> eraNames = getSection(path + "eras", "name").stream()
				.map(eraNameObject -> (String)eraNameObject)
				.collect(Collectors.toCollection(ArrayList::new));
		
		long eraZero = 1;
		
		return new TimeSystem(
			   worldname,
			   timeSystemName,
			   ticksPerSecond,
			   secondsPerMinute,
			   minutesPerHour,
			   hoursPerDay,
			   daysPerWeek,
			   daysPerMonth,
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
			   monthNames,
			   seasonNames,
			   eraNames
		);
	}

	public FileConfiguration reloadConfig()
	{
		cfg = super.reloadConfig();
		
		reload();
		
		return cfg;
	}
	
	private long getZero(String path)
	{
		return getLong(path + "zero");
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
