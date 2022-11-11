package me.m0dii.venturacalendar.base.events;

import me.m0dii.venturacalendar.game.gui.Calendar;
import me.m0dii.venturacalendar.game.gui.RealTimeCalendar;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class RealTimeCalendarCloseEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    private final Inventory inv;
    private final RealTimeCalendar calendar;
    private final Player player;

    public RealTimeCalendarCloseEvent(RealTimeCalendar cal, Inventory inv, Player p) {
        this.calendar = cal;
        this.inv = inv;
        this.player = p;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public RealTimeCalendar getCalendar() {
        return calendar;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inv;
    }
}
