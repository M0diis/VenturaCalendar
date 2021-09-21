package me.M0dii.venturacalendar.base.dateutils;

import me.M0dii.venturacalendar.VenturaCalendar;

import java.util.ArrayList;

public class DateCalculator
{
	final TimeSystemUtils tsUtils = VenturaCalendar.getInstance().getTimeSystemUtils();
	
	public Date fromMillis(TimeSystem timeSystem)
	{
		long millis = System.currentTimeMillis();
		
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
		
		ArrayList<Long> erasBegin = timeSystem.getErasBegin();
		ArrayList<Long> erasEnd   = timeSystem.getErasEnd();
		
		long ticksPerSecond 		  = (long) tsUtils.getTPU(DateEnum.SECOND, timeSystem);
		long ticksPerMinute 		  = (long) tsUtils.getTPU(DateEnum.MINUTE, timeSystem);
		long ticksPerHour 			  = (long) tsUtils.getTPU(DateEnum.HOUR, timeSystem);
		long ticksPerDay    		  = (long) tsUtils.getTPU(DateEnum.DAY, timeSystem);
		long ticksPerWeek   		  = (long) tsUtils.getTPU(DateEnum.WEEK, timeSystem);
		ArrayList<Long> ticksPerMonth = (ArrayList<Long>) tsUtils.getTPU(DateEnum.MONTH, timeSystem);
		long ticksPerYear  			  = (long) tsUtils.getTPU(DateEnum.YEAR, timeSystem);
		long rootTicks                = ticks;

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
		
		return new Date(timeSystem, rootTicks, tick, second, minute, hour, day, week, month, year, era);
	}
}
