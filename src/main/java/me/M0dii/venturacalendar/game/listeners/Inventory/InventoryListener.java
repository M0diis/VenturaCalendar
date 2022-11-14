package me.m0dii.venturacalendar.game.listeners.inventory;

import me.m0dii.venturacalendar.base.events.CalendarClickEvent;
import me.m0dii.venturacalendar.base.events.CalendarCloseEvent;
import me.m0dii.venturacalendar.base.events.RealTimeCalendarClickEvent;
import me.m0dii.venturacalendar.base.events.RealTimeCalendarCloseEvent;
import me.m0dii.venturacalendar.game.gui.Calendar;
import me.m0dii.venturacalendar.game.gui.RealTimeCalendar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        Inventory inv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();

        if (inv == null || !(inv.getHolder() instanceof Calendar cal)) {
            return;
        }

        e.setCancelled(true);

        Bukkit.getPluginManager().callEvent(new CalendarClickEvent(cal, inv, player, item));
    }

    @EventHandler
    public void onInventoryClickRealTimeCalendar(final InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        Inventory inv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();

        if (inv == null || !(inv.getHolder() instanceof RealTimeCalendar cal)) {
            return;
        }

        e.setCancelled(true);

        Bukkit.getPluginManager().callEvent(new RealTimeCalendarClickEvent(cal, e, inv, player, item));
    }

    @EventHandler
    public void onInventoryCloseCalendar(final InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();

        if (e.getPlayer() instanceof Player player) {

            if (inventory instanceof Calendar cal) {
                Bukkit.getPluginManager().callEvent(new CalendarCloseEvent(cal, inventory, player));
            }
        }
    }

    @EventHandler
    public void onInventoryCloseRealTimeCalendar(final InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();

        if (e.getPlayer() instanceof Player player) {

            if (inventory instanceof RealTimeCalendar cal) {
                Bukkit.getPluginManager().callEvent(new RealTimeCalendarCloseEvent(cal, inventory, player));
            }
        }
    }
}
