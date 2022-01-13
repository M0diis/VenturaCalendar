package me.m0dii.venturacalendar.game.gui;

import java.util.HashMap;

import me.m0dii.venturacalendar.VenturaCalendar;
import org.bukkit.entity.Player;

public class StorageUtils
{
	public void storeCalendar(Player player, Calendar calendar)
	{
		HashMap<Player, Storage> storages = VenturaCalendar.storages;
		
		Storage storage;
		
		if(storages.containsKey(player))
		{
			storage = storages.get(player);
			
			storages.remove(player);
		}
		else
		{
			storage = new Storage();
			
			storage.setStorageHolder(player);
		}
		
		storage.setCalendar(calendar);
		
		storages.put(player, storage);
		
		VenturaCalendar.storages = storages;
	}

}
