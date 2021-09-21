package me.M0dii.venturacalendar.base.dateutils;

import java.util.ArrayList;

public class TimeSystem
{
	private final String worldName;
	private final String name;
	
	// Date parameters.
	private final long ticksPerSecond;
	private final long secondsPerMinute;
	private final long minutesPerHour;
	private final long hoursPerDay;
	private final long daysPerWeek;
	private final ArrayList<Long> daysPerMonth;
	private final long monthsPerYear;
	private final ArrayList<Long> erasBegin;
	private final ArrayList<Long> erasEnd;

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
	private final ArrayList<String> dayNames;
	private final ArrayList<String> monthNames;
	private final ArrayList<String> eraNames;
	
	public TimeSystem(String worldname,
			          String name,
					  long ticksPerSecond,
					  long secondsPerMinute,
					  long minutesPerHour,
					  long hoursPerDay,
					  long daysPerWeek,
					  ArrayList<Long> daysPerMonth,
					  long monthsPerYear,
					  ArrayList<Long> erasBegin,
					  ArrayList<Long> erasEnd,
					  
					  long tickZero,
					  long secondZero,
					  long minuteZero,
					  long hourZero,
					  long dayZero,
					  long weekZero,
					  long monthZero,
					  long yearZero,
					  long eraZero,
					  
					  ArrayList<String> dayNames,
					  ArrayList<String> monthNames,
					  ArrayList<String> eraNames)
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
		this.monthNames = monthNames;
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
		this.monthNames			= timeSystem.getMonthNames();
		this.eraNames			= timeSystem.getEraNames();
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

	public ArrayList<Long> getDaysPerMonth() {
		return daysPerMonth;
	}

	public long getMonthsPerYear() {
		return monthsPerYear;
	}

	public ArrayList<Long> getErasBegin() {
		return erasBegin;
	}

	public ArrayList<Long> getErasEnd() {
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
	
	public ArrayList<String> getDayNames() {
		return dayNames;
	}
	
	public ArrayList<String> getMonthNames() {
		return monthNames;
	}
	
	public ArrayList<String> getEraNames() {
		return eraNames;
	}
	
}
