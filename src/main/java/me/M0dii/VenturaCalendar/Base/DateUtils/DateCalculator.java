package me.M0dii.VenturaCalendar.Base.DateUtils;

import me.M0dii.VenturaCalendar.VenturaCalendar;

import java.util.ArrayList;

public class DateCalculator
{
	TimeSystemUtils timeSystemUtils = VenturaCalendar.getTimeSystemUtils();
	
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
			

		long ticksPerSecond 			= (long) timeSystemUtils.getTicksPerUnit(DateEnum.second, timeSystem);
		long ticksPerMinute 			= (long) timeSystemUtils.getTicksPerUnit(DateEnum.minute, timeSystem);
		long ticksPerHour 				= (long) timeSystemUtils.getTicksPerUnit(DateEnum.hour, timeSystem);
		long ticksPerDay    			= (long) timeSystemUtils.getTicksPerUnit(DateEnum.day, timeSystem);
		long ticksPerWeek   			= (long) timeSystemUtils.getTicksPerUnit(DateEnum.week, timeSystem);
		ArrayList<Long> ticksPerMonth 	= (ArrayList<Long>) timeSystemUtils.getTicksPerUnit(DateEnum.month, timeSystem);
		long ticksPerYear  				= (long) timeSystemUtils.getTicksPerUnit(DateEnum.year, timeSystem);
		long rootTicks = ticks;

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
