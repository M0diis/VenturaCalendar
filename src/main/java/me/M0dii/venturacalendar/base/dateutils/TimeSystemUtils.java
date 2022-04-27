package me.m0dii.venturacalendar.base.dateutils;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeSystemUtils
{
	public HashMap<TimeSystemEnum, Object> toHashMap(TimeSystem timeSystem)
	{
		timeSystem = new TimeSystem(timeSystem);
		
		HashMap<TimeSystemEnum, Object> timeSystems = new HashMap<>();
		
		timeSystems.put(TimeSystemEnum.NAME, timeSystem.getName());
		
		timeSystems.put(TimeSystemEnum.TICKS_PER_SECOND, timeSystem.getTicksPerSecond());
		timeSystems.put(TimeSystemEnum.SECONDS_PER_MINUTE, timeSystem.getSecondsPerMinute());
		timeSystems.put(TimeSystemEnum.MINUTES_PER_HOUR, timeSystem.getMinutesPerHour());
		timeSystems.put(TimeSystemEnum.HOURS_PER_DAY, timeSystem.getHoursPerDay());
		timeSystems.put(TimeSystemEnum.DAYS_PER_WEEK, timeSystem.getDaysPerWeek());
		timeSystems.put(TimeSystemEnum.DAYS_PER_MONTH, timeSystem.getDaysPerMonth());
		timeSystems.put(TimeSystemEnum.MONTHS_PER_YEARS, timeSystem.getMonthsPerYear());
		timeSystems.put(TimeSystemEnum.ERAS_BEGIN, timeSystem.getErasBegin());
		timeSystems.put(TimeSystemEnum.ERAS_END, timeSystem.getErasEnd());
		
		timeSystems.put(TimeSystemEnum.TICK_ZERO, timeSystem.getTickZero());
		timeSystems.put(TimeSystemEnum.SECOND_ZERO, timeSystem.getSecondZero());
		timeSystems.put(TimeSystemEnum.MINUTE_ZERO, timeSystem.getMinuteZero());
		timeSystems.put(TimeSystemEnum.HOUR_ZERO, 1);
		timeSystems.put(TimeSystemEnum.DAY_ZERO, 1);
		timeSystems.put(TimeSystemEnum.WEEK_ZERO, 1);
		timeSystems.put(TimeSystemEnum.MONTH_ZERO, timeSystem.getMonthZero());
		timeSystems.put(TimeSystemEnum.ERA_ZERO, timeSystem.getEraZero());
		
		return timeSystems;
	}
	
	public Object getTPU(DateEnum unit, TimeSystem timeSystem)
	{
		long ticksPerSecond = timeSystem.getTicksPerSecond();
		long ticksPerMinute = ticksPerSecond * timeSystem.getSecondsPerMinute();
		long ticksPerHour 	= ticksPerMinute * timeSystem.getMinutesPerHour();
		long ticksPerDay    = ticksPerHour   * timeSystem.getHoursPerDay();
		long ticksPerWeek   = ticksPerDay    * timeSystem.getDaysPerWeek();
		
		ArrayList<Long> ticksPerMonth = new ArrayList<>();
		
		for(long daysThisMonth : timeSystem.getDaysPerMonth())
			ticksPerMonth.add(ticksPerDay * daysThisMonth);
		
		long ticksPerYear  = 0;
		
		for(long ticksThisMonth : ticksPerMonth)
			ticksPerYear = ticksPerYear + ticksThisMonth;
		
		switch(unit)
		{
			case TICK:
				return 1;
			case SECOND:
				return ticksPerSecond;
			case MINUTE:
				return ticksPerMinute;
			case HOUR:
				return ticksPerHour;
			case DAY:
				return ticksPerDay;
			case WEEK:
				return ticksPerWeek;
			case MONTH:
				return ticksPerMonth;
			case YEAR:
				return ticksPerYear;
			default:
				return 0;
		}
	}

}
