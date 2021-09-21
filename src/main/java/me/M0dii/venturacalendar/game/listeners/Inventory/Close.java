package me.M0dii.venturacalendar.game.listeners.Inventory;

import me.M0dii.venturacalendar.VenturaCalendar;
import me.M0dii.venturacalendar.game.gui.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class Close
{
	public Close(InventoryCloseEvent event, VenturaCalendar plugin)
	{
		Inventory inventory = event.getInventory();
		
		if(event.getPlayer() instanceof Player player)
		{
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
