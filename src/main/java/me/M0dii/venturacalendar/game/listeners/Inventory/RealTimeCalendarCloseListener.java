package me.m0dii.venturacalendar.game.listeners.inventory;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.events.CalendarCloseEvent;
import me.m0dii.venturacalendar.base.events.RealTimeCalendarCloseEvent;
import me.m0dii.venturacalendar.game.gui.Storage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RealTimeCalendarCloseListener implements Listener {
    @EventHandler
    public void onCalendarClose(final RealTimeCalendarCloseEvent e) {
        Storage storage = VenturaCalendar.storages.get(e.getPlayer());

        if (storage == null) {
            return;
        }

        if (e.getInventory().equals(storage.getCalendar().getInventory())) {
            storage.setCalendar(null);
        }

        if (storage.allNull()) {
            VenturaCalendar.storages.remove(e.getPlayer());
        }
    }
}
