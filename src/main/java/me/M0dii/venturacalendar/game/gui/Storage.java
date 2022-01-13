package me.m0dii.venturacalendar.game.gui;

import org.bukkit.entity.Player;

public class Storage
{
	Player storageHolder;
	
	Calendar calendar;
	
	public Player getHolder()
	{
		return storageHolder;
	}
	
	public void setStorageHolder(Player holder)
	{
		this.storageHolder = holder;
	}
	
	public Calendar getCalendar()
	{
		return calendar;
	}
	
	public void setCalendar(Calendar calendar)
	{
		this.calendar = calendar;
	}

	public boolean allNull()
	{
		return calendar == null;
	}

}
