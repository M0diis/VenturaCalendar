package me.M0dii.VenturaCalendar.Base.ConfigUitls;

import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeConfig extends Config implements ConfigUtils
{
	FileConfiguration config;
	
	public TimeConfig()
	{
		super(VenturaCalendar.instance.getDataFolder(), "TimeConfig.yml");
		
		config = super.loadConfig();
		
		reload();
	}
	
	HashMap<String, TimeSystem> timeSystems = new HashMap<>();
	
	private void reload()
	{
		ConfigurationSection timeSystemsName = config.getConfigurationSection("Time-Systems");
		
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
		//Tick
		long tickZero		= 0;
		//Second
		long ticksPerSecond;
		long secondZero		= 0;
		
		//Minute
		long secondsPerMinute;
		long minuteZero		= 0;
		
		//Hour
		long minutesPerHour;
		long hourZero;
		
		//Day
		long hoursPerDay;
		long dayZero;
		
		//Week
		long daysPerWeek;
		long weekZero;
		
		ArrayList<String> dayNames 		= new ArrayList<>();
		
		//Month
		ArrayList<Long> daysPerMonth	= new ArrayList<>();
		long monthZero;
		ArrayList<String> monthNames	= new ArrayList<>();
		
		//Year
		long monthsPerYear;
		long yearZero;
		
		//Era
		ArrayList<Long> erasBegin		= new ArrayList<>();
		long eraZero;
		ArrayList<String> eraNames		= new ArrayList<>();
		ArrayList<Long> erasEnd			= new ArrayList<>();
		
		String defaultPath = "Time-Systems." + timeSystemName + ".";
		String path;
		
		String worldname = getString(defaultPath + "world-name");
		
		// Tick
		path = defaultPath + "tick.";
		
		// Second
		path = defaultPath + "second.";
		
		ticksPerSecond = getLong(path + "ticksPerSecond");
		
		// Minute
		path = defaultPath + "minute.";
		
		secondsPerMinute = getLong(path + "secondsPerMinute");
		
		// Hour
		path = defaultPath + "hour.";
		
		minutesPerHour = getLong(path + "minutesPerHour");
		hourZero = 1;
		
		// Day
		path = defaultPath + "day.";
		
		hoursPerDay = getLong(path + "hoursPerDay");
		dayZero = 1;
		
		ArrayList<Object> dayNamesObject = getSection(path + "dayNames", "name");
		
		for(Object dayNameObject : dayNamesObject)
			dayNames.add((String) dayNameObject);
		
		// Week
		path = defaultPath + "week.";
		
		daysPerWeek = getLong(path + "daysPerWeek");
		weekZero = 1;
		
		// Month
		path = defaultPath + "month.";
		
		ArrayList<Object> daysPerMonthObject = getSection(path + "daysPerMonth", "days");
		
		for(Object daysThisMonthObject : daysPerMonthObject)
				daysPerMonth.add(Long.valueOf((String) daysThisMonthObject));

		monthZero = 1;
		
		ArrayList<Object> monthNamesObject = getSection(path + "daysPerMonth", "name");
		
		for(Object monthNameObject : monthNamesObject)
			monthNames.add((String) monthNameObject);
		
		// Year
		path = defaultPath + "year.";
		
		monthsPerYear = getLong(path + "monthsPerYear");
		yearZero = getZero(path);
		
		// Era
		path = defaultPath + "era.";
		
		ArrayList<Object> erasBeginObjects = getSection(path + "eras", "startYear");
		
		for(Object erasBeginObject : erasBeginObjects)
			erasBegin.add(Long.valueOf((String) erasBeginObject));
			
		ArrayList<Object> erasEndObjects = getSection(path + "eras", "endYear");
		
		for(Object erasEndObject : erasEndObjects)
			erasEnd.add(Long.valueOf((String) erasEndObject));
		
		ArrayList<Object> erasNameObjects = getSection(path + "eras", "name");
		
		for(Object eraNameObject : erasNameObjects)
			eraNames.add((String) eraNameObject);
			
		eraZero = 1;
		
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
			   eraNames
		);
	}

	public FileConfiguration reloadConfig()
	{
		config = super.reloadConfig();
		
		reload();
		
		return config;
	}
	
	private long getZero(String path)
	{
		return getLong(path + "zero");
	}
	
	private ArrayList<Object> getSection(String path, String value)
	{
		ArrayList<Object> names = new ArrayList<>();
		
		ConfigurationSection section = config.getConfigurationSection(path);
		
		if (section != null)
			for(String key : section.getKeys(false))
				names.add(getString(path + "." + key + "." + value));

		return names;		
	}
	
	@Override
	public String getString(String path)
	{
		return ChatColor.translateAlternateColorCodes('&', config.getString(path, ""));
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
		List<String> list = config.getStringList(path);
		
		for(int index = 0; index < list.size(); index++)
			list.set(index, ChatColor.translateAlternateColorCodes('&', list.get(index)));
			
		return list;
	}
}
