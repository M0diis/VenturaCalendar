package me.M0dii.VenturaCalendar.Game.Listeners.Inventory;

import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Game.GUI.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class Close
{
	public Close(InventoryCloseEvent event)
	{
		Inventory inventory = event.getInventory();
		
		if(event.getPlayer() instanceof Player)
		{
			Player player = (Player) event.getPlayer();
			Storage storage = VenturaCalendar.storages.get(player);
			
			if(storage != null)
			{
				if(inventory.equals(storage.getCalendar().getInventory()))
					storage.setCalendar(null);
				
				if(storage.allNull())
					VenturaCalendar.storages.remove(player);
			}
		}
	}
}
