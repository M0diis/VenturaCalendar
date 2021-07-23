package me.M0dii.VenturaCalendar.Base.DateUtils;

import me.M0dii.VenturaCalendar.VenturaCalendar;

import java.util.ArrayList;
import java.util.HashMap;

public class DateUtils {
	
	/*
	 * Puts all date properties from DateEnum into a HashMap.
	 */
	public HashMap<DateEnum, Object> toHashMap(Date date)
	{
		date = new Date(date);
		
		HashMap<DateEnum, Object> dateMap = new HashMap<>();
		
		dateMap.put(DateEnum.TIMESYSTEM, date.getTimeSystem());
		
		dateMap.put(DateEnum.TICK, date.getTick());
		dateMap.put(DateEnum.SECOND, date.getSecond());
		dateMap.put(DateEnum.MINUTE, date.getMinute());
		dateMap.put(DateEnum.HOUR, date.getHour());
		dateMap.put(DateEnum.DAY, date.getDay());
		dateMap.put(DateEnum.WEEK, date.getWeek());
		dateMap.put(DateEnum.MONTH, date.getMonth());
		dateMap.put(DateEnum.year, date.getYear());
		dateMap.put(DateEnum.ERA, date.getEra());
		
		return dateMap;
	}

	public Date getNullDate(TimeSystem timeSystem)
	{
		return new Date(timeSystem, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	public Date addZeroPoints(Date date)
	{
		date = new Date(date);
		
		TimeSystem timeSystem = new TimeSystem(date.getTimeSystem());
		
		date.setTick	(date.getTick() 	+ timeSystem.getTickZero());
		date.setSecond	(date.getSecond() 	+ timeSystem.getSecondZero());
		date.setMinute	(date.getMinute() 	+ timeSystem.getMinuteZero());
		date.setHour	(date.getHour() 	+ timeSystem.getHourZero());
		date.setDay		(date.getDay() 		+ timeSystem.getDayZero());
		date.setWeek	(date.getWeek() 	+ timeSystem.getWeekZero());
		date.setMonth	(date.getMonth() 	+ timeSystem.getMonthZero());
		date.setYear	(date.getYear() 	+ timeSystem.getYearZero());
		date.setEra		(date.getEra() 		+ timeSystem.getEraZero());
		
		return date;
	}

	public Date removeZeroPoints(Date date)
	{
		date = new Date(date);
		
		TimeSystem timeSystem = new TimeSystem(date.getTimeSystem());
		
		date.setTick	(date.getTick() 	- timeSystem.getTickZero());
		date.setSecond	(date.getSecond() 	- timeSystem.getSecondZero());
		date.setMinute	(date.getMinute() 	- timeSystem.getMinuteZero());
		date.setHour	(date.getHour() 	- timeSystem.getHourZero());
		date.setDay		(date.getDay() 		- timeSystem.getDayZero());
		date.setWeek	(date.getWeek() 	- timeSystem.getWeekZero());
		date.setMonth	(date.getMonth() 	- timeSystem.getMonthZero());
		date.setYear	(date.getYear() 	- timeSystem.getYearZero());
		date.setEra		(date.getEra() 		- timeSystem.getEraZero());
		
		return date;
	}
	
	/*
	 * Methods to calculate up or down a single parameter from a given date, with the date timeSystem.
	 * With maximum and minimum check.
	 */
	public Date up(DateEnum unit, int count, Date date)
	{
		return calculate(unit, count, date, false);
	}
	
	public Date down(DateEnum unit, int count, Date date)
	{
		return calculate(unit, count, date, true);
	}
	
	private Date calculate(DateEnum unit, int count, Date date, boolean down)
	{
		date = new Date(date);
		
		TimeSystem timeSystem = new TimeSystem(date.getTimeSystem());
		
		DateCalculator dateCalc = VenturaCalendar.getDateCalculator();
		long ticks = date.getRootTicks();
		
		long ticksPerSecond = timeSystem.getTicksPerSecond();
		long ticksPerMinute = ticksPerSecond * timeSystem.getSecondsPerMinute();
		long ticksPerHour 	= ticksPerMinute * timeSystem.getMinutesPerHour();
		long ticksPerDay    = ticksPerHour   * timeSystem.getHoursPerDay();
		long ticksPerWeek   = ticksPerDay    * timeSystem.getDaysPerWeek();
		
		ArrayList<Long> ticksPerMonth = new ArrayList<>();
		
		for(long daysThisMonth : timeSystem.getDaysPerMonth())
			ticksPerMonth.add(ticksPerDay * daysThisMonth);
		
		long ticksPerYear = 0;
		
		for(long ticksThisMonth : ticksPerMonth)
			ticksPerYear = ticksPerYear + ticksThisMonth;
		
		switch(unit)
		{
			case TICK:
				if(!down) return dateCalc.fromTicks(ticks + count, timeSystem);
				return dateCalc.fromTicks(ticks - count, timeSystem);
			
			case SECOND:
				if(!down) return dateCalc.fromTicks(ticks + (ticksPerSecond * count),  timeSystem);
				return dateCalc.fromTicks(ticks - (ticksPerSecond * count),  timeSystem);
			
			case MINUTE:
				if(!down) return dateCalc.fromTicks(ticks + (ticksPerMinute * count), timeSystem);
				return dateCalc.fromTicks(ticks - (ticksPerMinute * count), timeSystem);
			
			case HOUR:
				if(!down) return dateCalc.fromTicks(ticks + (ticksPerHour * count), timeSystem);
				return dateCalc.fromTicks(ticks - (ticksPerHour * count), timeSystem);
			
			case DAY:
				if(!down) return dateCalc.fromTicks(ticks + (ticksPerDay * count), timeSystem);
				return dateCalc.fromTicks(ticks - (ticksPerDay * count), timeSystem);
			
			case WEEK:
				if(!down) return dateCalc.fromTicks(ticks + (ticksPerWeek * count), timeSystem);
				return dateCalc.fromTicks(ticks - (ticksPerWeek * count), timeSystem);
			
			case MONTH:
				if(!down) return dateCalc.fromTicks(ticks + ticksPerMonth.get((int) date.getMonth() - 1), timeSystem);
				return dateCalc.fromTicks(ticks - ticksPerMonth.get((int) date.getMonth() - 1), timeSystem);
			
			case year:
				if(!down) return dateCalc.fromTicks(ticks + (ticksPerYear * count), timeSystem);
				return dateCalc.fromTicks(ticks - (ticksPerYear * count), timeSystem);
			
			default:
				return date;
		}
	}
	
	public long getDayOfWeek(Date date)
	{
		if(date == null)
			return 0L;
		
		date = new Date(date);
		
		if(!date.getTimeSystem().getWorldName()
				.equalsIgnoreCase("real-time") && date.getMonth() != 6)
			date.setDay(date.getDay() + date.getTimeSystem().getDayZero());
		
	    int cc = (int) (date.getYear() / 100);
	    int yy = (int) (date.getYear() - ((date.getYear() / 100) * 100));

	    int c = (cc / 4) - 2 * cc - 1;
	    int y = 5 * yy / 4;
	    int m = (int) (26 * (date.getMonth() + 1) / 10);
	    int d = (int) date.getDay();
		
		return (int) ((c + y + m + d) % date.getTimeSystem().getDaysPerWeek());
	}
}
