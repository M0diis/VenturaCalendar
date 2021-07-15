package me.M0dii.VenturaCalendar.Base.DateUtils;

import me.M0dii.VenturaCalendar.VenturaCalendar;

public class Date
{
	private final TimeSystem timeSystem;
	long rootTicks;
	
	private long tick;
	private long second;
	private long minute;
	private long hour;
	private long day;
	private long week;
	private long month;
	private long year;
	private long era;

	public Date(TimeSystem timeSystem,
			long rootTicks,
			long tick,
			long second,
			long minute,
			long hour,
			long day,
			long week,
			long month,
			long year,
			long era)
	{
		
		this.timeSystem = timeSystem;
		this.rootTicks  = rootTicks;
		
		this.tick 	 	= tick;
		this.second  	= second;
		this.minute  	= minute;
		this.hour 	 	= hour;
		this.day		= day;
		this.week		= week;
		this.month 	 	= month;
		this.year 	 	= year;
		this.era 	 	= era;
		
	}
	
	public Date(Date date)
	{
		this.timeSystem = date.getTimeSystem();
		this.rootTicks 	= date.getRootTicks();
		
		this.tick 	= date.getTick();
		this.second = date.getSecond();
		this.minute = date.getMinute();
		this.hour 	= date.getHour();
		this.day	= date.getDay();
		this.week	= date.getWeek();
		this.month 	= date.getMonth();
		this.year 	= date.getYear();
		this.era 	= date.getEra();
	}
	
	public void toNullDate()
	{
		Date date = VenturaCalendar.getDateUtils().getNullDate(timeSystem);
		this.rootTicks = date.getRootTicks();
		
		this.tick = date.getTick();
		this.second = date.getSecond();
		this.minute = date.getMinute();
		this.hour = date.getHour();
		this.day = date.getDay();
		this.week = date.getWeek();
		this.month = date.getMonth();
		this.year = date.getYear();
		this.era = date.getEra();
	}

	public TimeSystem getTimeSystem() {
		return timeSystem;
	}
	
	public long getRootTicks() {
		return rootTicks;
	}
	
	public long getTick() {
		return tick;
	}

	public void setTick(long tick) {
		this.tick = tick;
	}

	public long getSecond() {
		return second;
	}

	public void setSecond(long second) {
		this.second = second;
	}

	public long getMinute() {
		return minute;
	}

	public void setMinute(long minute) {
		this.minute = minute;
	}

	public long getHour() {
		return hour;
	}

	public void setHour(long hour) {
		this.hour = hour;
	}

	public long getDay() {
		return day;
	}

	public void setDay(long day)
	{
		this.day = day;
	}

	public long getWeek()
	{
		return week;
	}

	public void setWeek(long week)
	{
		this.week = week;
	}

	public long getMonth()
	{
		return month;
	}
	
	public String getMonthName()
	{
		return this.getTimeSystem().getMonthNames().get((int)this.month);
	}
	
	public String getDayName()
	{
		return this.getTimeSystem().getDayNames().get((int)this.day);
	}
	
	public String getEraName()
	{
		return this.getTimeSystem().getEraNames().get((int)this.era);
	}

	public void setMonth(long month) {
		this.month = month;
	}

	public long getYear() {
		return year;
	}

	public void setYear(long year) {
		this.year = year;
	}

	public long getEra() {
		return era;
	}

	public void setEra(long era) {
		this.era = era;
	}
	
}
