package me.m0dii.venturacalendar.base.events;

import lombok.Getter;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.base.dateutils.VenturaCalendarDate;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class NewDayEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final TimeSystem timeSystem;
    private final World world;
    private final VenturaCalendarDate date;
    private boolean isCancelled;

    public NewDayEvent(TimeSystem ts, World world, VenturaCalendarDate date) {
        this.timeSystem = ts;
        this.world = world;
        this.date = date;
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

}
