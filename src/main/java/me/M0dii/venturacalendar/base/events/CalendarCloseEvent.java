package me.m0dii.venturacalendar.base.events;

import lombok.Getter;
import me.m0dii.venturacalendar.game.gui.Calendar;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@Getter
public class CalendarCloseEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Inventory inv;
    private final Calendar calendar;
    private final Player player;
    private boolean isCancelled;

    public CalendarCloseEvent(Calendar cal, Inventory inv, Player p) {
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

    public Inventory getInventory() {
        return inv;
    }
}
