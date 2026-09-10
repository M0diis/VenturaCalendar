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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public final class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) {
            return;
        }

        Inventory top = e.getView().getTopInventory();

        if (!(top.getHolder() instanceof Calendar cal)) {
            return;
        }

        e.setCancelled(true);

        if (e.getClickedInventory() == top) {
            Bukkit.getPluginManager().callEvent(new CalendarClickEvent(cal, e, top, player, e.getCurrentItem()));
        }
    }

    @EventHandler
    public void onInventoryClickRealTimeCalendar(final InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) {
            return;
        }

        Inventory top = e.getView().getTopInventory();

        if (!(top.getHolder() instanceof RealTimeCalendar cal)) {
            return;
        }

        e.setCancelled(true);

        if (e.getClickedInventory() == top) {
            Bukkit.getPluginManager().callEvent(new RealTimeCalendarClickEvent(cal, e, top, player, e.getCurrentItem()));
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        Inventory top = e.getView().getTopInventory();

        if (top.getHolder() instanceof Calendar || top.getHolder() instanceof RealTimeCalendar) {
            if (e.getRawSlots().stream().anyMatch(slot -> slot < top.getSize())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryCloseCalendar(final InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();

        if (e.getPlayer() instanceof Player player) {

            if (inventory.getHolder() instanceof Calendar cal) {
                Bukkit.getPluginManager().callEvent(new CalendarCloseEvent(cal, inventory, player));
            }
        }
    }

    @EventHandler
    public void onInventoryCloseRealTimeCalendar(final InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();

        if (e.getPlayer() instanceof Player player) {

            if (inventory.getHolder() instanceof RealTimeCalendar cal) {
                Bukkit.getPluginManager().callEvent(new RealTimeCalendarCloseEvent(cal, inventory, player));
            }
        }
    }
}
