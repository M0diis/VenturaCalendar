package me.M0dii.VenturaCalendar.Game.GUI;

import java.util.HashMap;

import me.M0dii.VenturaCalendar.VenturaCalendar;
import org.bukkit.entity.Player;

public class StorageUtils
{
	public void storageCalendar(Player player, Calendar calendar)
	{
		// Gets the map with all storages
		HashMap<Player, Storage> storages = VenturaCalendar.storages;
		
		Storage storage;
		
		// Checks if the storage exists
		if(storages.containsKey(player))
		{
			// Gets the existing storage out of the map
			storage = storages.get(player);
			// Deletes the storage out of the map
			storages.remove(player);
			
		}
		else
		{
			// Creates a new Storage instance
			storage = new Storage();
			// Sets the holder of the storage
			storage.setStorageHolder(player);
		}
		
		// Updates or sets a new calendar in the storage
		storage.setCalendar(calendar);
		
		// Puts the new or edited storage in the storage map
		storages.put(player, storage);
		
		// Sets the storage map to the edited one
		VenturaCalendar.storages = storages;
	}

}
