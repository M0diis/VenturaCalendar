package me.m0dii.venturacalendar.base.dateutils;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.utils.Messenger;

import java.util.List;
import java.util.TimeZone;

public class DateCalculator
{
	private final VenturaCalendar plugin;
	private final TimeSystemUtils tsUtils;
	
	public DateCalculator(VenturaCalendar plugin)
    {
        this.plugin = plugin;
		this.tsUtils = plugin.getTimeSystemUtils();
    }
	
	public Date fromMillis(TimeSystem timeSystem)
	{
		long millis = System.currentTimeMillis();
		
		if(timeSystem.useTimeZone())
		{
			TimeZone timeZone = TimeZone.getTimeZone(timeSystem.getTimeZone());
			
			if(timeZone == null)
			{
				Messenger.log(Messenger.Level.WARN, "TimeZone '" + timeSystem.getTimeZone() + "' not found!");
			}
			else
			{
				millis += timeZone.getRawOffset();
			}
		}
		
		long ticks = millis / 50 - 22320000;
		
		return fromTicks(ticks, timeSystem);
	}
	
	public Date fromTicks(long ticks, TimeSystem timeSystem)
	{
		long tick	 = 0;
		long second  = 0;
		long minute	 = 0;
		long hour  	 = 0;
		long day  	 = 0;
		long week  	 = 0;
		long month 	 = 0;
		long year  	 = 0;
		long era	 = 0;
		
		List<Long> erasBegin = timeSystem.getErasBegin();
		List<Long> erasEnd   = timeSystem.getErasEnd();
		
		long ticksPerSecond 		  = (long) tsUtils.getTPU(DateEnum.SECOND, timeSystem);
		long ticksPerMinute 		  = (long) tsUtils.getTPU(DateEnum.MINUTE, timeSystem);
		long ticksPerHour 			  = (long) tsUtils.getTPU(DateEnum.HOUR, timeSystem);
		long ticksPerDay    		  = (long) tsUtils.getTPU(DateEnum.DAY, timeSystem);
		long ticksPerWeek   		  = (long) tsUtils.getTPU(DateEnum.WEEK, timeSystem);
		List<Long> ticksPerMonth = (List<Long>) tsUtils.getTPU(DateEnum.MONTH, timeSystem);
		long ticksPerYear  			  = (long) tsUtils.getTPU(DateEnum.YEAR, timeSystem);
		long rootTicks                = ticks;
		
		long dayTicks = 24000;
		
		ticks += (timeSystem.getDayZero() * dayTicks)
				+ (timeSystem.getWeekZero() * (7 * dayTicks));
		
		for(int i = 0, m = 0; i < timeSystem.getMonthZero(); i++, m++)
		{
			if(m > timeSystem.getMonths().size() - 1)
			{
				m = 0;
			}
			
			ticks += (timeSystem.getMonths().get(m).getDays() * dayTicks);
		}
		
		year = ticks / ticksPerYear;
		ticks = ticks - year * ticksPerYear;
		
		for(long ticksThisMonth : ticksPerMonth)
		{
			if(ticks / ticksThisMonth > 0)
			{
				month++;
				ticks = ticks - ticksThisMonth;
			}
		}
		
		week = ticks / ticksPerWeek;
		
		day = ticks / ticksPerDay;
		ticks = ticks - day * ticksPerDay;
		
		hour = ticks / ticksPerHour;
		ticks = ticks - hour * ticksPerHour;
		
		minute = ticks / ticksPerMinute;
		ticks = ticks - minute * ticksPerMinute;
		
		second = ticks / ticksPerSecond;
		ticks = ticks - second * ticksPerSecond;
		
		tick = ticks;
		ticks = ticks - tick;
		
		tick = ticks;
		
		for(long eraBegin : erasBegin)
		{
			int index   = erasBegin.indexOf(eraBegin);
			eraBegin 	= eraBegin  		 - timeSystem.getYearZero();
			long eraEnd = erasEnd.get(index) - timeSystem.getYearZero();
			
			if(year >= eraBegin && year <= eraEnd)
				era = index;
		}
		
		return new Date(timeSystem, rootTicks,
				tick, second, minute, hour, day, week, month, year, era);
	}
}
