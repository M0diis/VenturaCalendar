package me.m0dii.venturacalendar.base.dateutils;

import java.util.List;
import java.util.Optional;

public class TimeSystem
{
	private final String worldName;
	private final String name;
	private Optional<String> timeZone;
	private boolean useTimeZone = false;
	private boolean realTime = false;
	
	// Date parameters.
	private final long ticksPerSecond;
	private final long secondsPerMinute;
	private final long minutesPerHour;
	private final long hoursPerDay;
	private final long daysPerWeek;
	private final List<Long> daysPerMonth;
	private final long monthsPerYear;
	private final List<Long> erasBegin;
	private final List<Long> erasEnd;

	// Zero points
	private final long tickZero;
	private final long secondZero;
	private final long minuteZero;
	private final long hourZero;
	private final long dayZero;
	private final long weekZero;
	private final long monthZero;
	private final long yearZero;
	private final long eraZero;
	
	// Date parameter names.
	private final List<Month> months;
	private final List<String> dayNames;
	private final List<String> eraNames;
	
	public TimeSystem(String worldname,
			          String name,
					  long ticksPerSecond,
					  long secondsPerMinute,
					  long minutesPerHour,
					  long hoursPerDay,
					  long daysPerWeek,
					  List<Long> daysPerMonth,
					  long monthsPerYear,
					  List<Long> erasBegin,
					  List<Long> erasEnd,
					  
					  long tickZero,
					  long secondZero,
					  long minuteZero,
					  long hourZero,
					  long dayZero,
					  long weekZero,
					  long monthZero,
					  long yearZero,
					  long eraZero,
					
					  List<String> dayNames,
					  List<Month> months,
					  List<String> eraNames)
	{
		this.worldName          = worldname;
		this.name 				= name;
		
		this.ticksPerSecond 	= ticksPerSecond;
		this.secondsPerMinute 	= secondsPerMinute;
		this.minutesPerHour	 	= minutesPerHour;
		this.hoursPerDay 		= hoursPerDay;
		this.daysPerWeek 		= daysPerWeek;
		this.daysPerMonth 		= daysPerMonth;
		this.monthsPerYear 		= monthsPerYear;
		this.erasBegin 			= erasBegin;
		this.erasEnd 			= erasEnd;
		
		this.tickZero 			= tickZero;
		this.secondZero 		= secondZero;
		this.minuteZero 		= minuteZero;
		this.hourZero 			= hourZero;
		this.dayZero 			= dayZero;
		this.weekZero 			= weekZero;
		this.monthZero 			= monthZero;
		this.yearZero 			= yearZero;
		this.eraZero 			= eraZero;
		
		this.dayNames = dayNames;
		this.months = months;
		this.eraNames = eraNames;
	}
	
	public TimeSystem(TimeSystem timeSystem)
	{
		this.worldName = timeSystem.getWorldName();
		this.name = timeSystem.getName();
		
		this.ticksPerSecond 	= timeSystem.getTicksPerSecond();
		this.secondsPerMinute 	= timeSystem.getSecondsPerMinute();
		this.minutesPerHour 	= timeSystem.getMinutesPerHour();
		this.hoursPerDay 		= timeSystem.getHoursPerDay();
		this.daysPerWeek 		= timeSystem.getDaysPerWeek();
		this.daysPerMonth 		= timeSystem.getDaysPerMonth();
		this.monthsPerYear 		= timeSystem.getMonthsPerYear();
		this.erasBegin 			= timeSystem.getErasBegin();
		this.erasEnd 			= timeSystem.getErasEnd();
		
		this.tickZero 			= timeSystem.getTickZero();
		this.secondZero 		= timeSystem.getSecondZero();
		this.minuteZero 		= timeSystem.getMinuteZero();
		this.hourZero 			= timeSystem.getHourZero();
		this.dayZero 			= timeSystem.getDayZero();
		this.weekZero 			= timeSystem.getWeekZero();
		this.monthZero 			= timeSystem.getMonthZero();
		this.yearZero 			= timeSystem.getYearZero();
		this.eraZero 			= timeSystem.getEraZero();
		
		this.dayNames 			= timeSystem.getDayNames();
		this.months			    = timeSystem.getMonths();
		this.eraNames			= timeSystem.getEraNames();
		
		this.timeZone = timeSystem.getTimeZoneOptional();
		this.useTimeZone = timeSystem.useTimeZone();
	}
	
	public void setTimeZone(String timeZone)
	{
		this.timeZone = Optional.of(timeZone);
	}
	
	public String getTimeZone()
    {
        return timeZone.orElse("");
    }
	
	public Optional<String> getTimeZoneOptional()
	{
		return timeZone;
	}
	
	public void setUseTimeZone(boolean useTimeZone)
    {
        this.useTimeZone = useTimeZone;
    }
	
	public void setRealTime(boolean realTime)
	{
		this.realTime = realTime;
	}
	
	public boolean isRealTime()
	{
		return realTime;
	}
	
	public boolean useTimeZone()
	{
		return this.useTimeZone;
	}
	
	public String getName() {
		return name;
	}
	
	public String getWorldName()
	{
		return this.worldName;
	}
	
	public long getTicksPerSecond() {
		return ticksPerSecond;
	}

	public long getSecondsPerMinute() {
		return secondsPerMinute;
	}

	public long getMinutesPerHour() {
		return minutesPerHour;
	}

	public long getHoursPerDay() {
		return hoursPerDay;
	}

	public long getDaysPerWeek()
	{
		if(daysPerWeek > 8)
			return 8;
		
		return daysPerWeek;
	}

	public List<Long> getDaysPerMonth() {
		return daysPerMonth;
	}

	public long getMonthsPerYear() {
		return monthsPerYear;
	}

	public List<Long> getErasBegin() {
		return erasBegin;
	}

	public List<Long> getErasEnd() {
		return erasEnd;
	}
	
	public long getTickZero() {
		return tickZero;
	}

	public long getSecondZero() {
		return secondZero;
	}

	public long getMinuteZero() {
		return minuteZero;
	}

	public long getHourZero() {
		return hourZero;
	}

	public long getDayZero() {
		return dayZero;
	}

	public long getWeekZero() {
		return weekZero;
	}

	public long getMonthZero() {
		return monthZero;
	}

	public long getYearZero() {
		return yearZero;
	}

	public long getEraZero() {
		return eraZero;
	}
	
	public List<String> getDayNames()
	{
		return dayNames;
	}
	
	public List<Month> getMonths()
	{
		return months;
	}
	
	public Month getMonth(String name)
	{
		for(Month month : months)
		{
			if(month.getName().equals(name))
				return month;
		}
		
		return null;
	}
	
	public List<String> getEraNames()
	{
		return eraNames;
	}
}
