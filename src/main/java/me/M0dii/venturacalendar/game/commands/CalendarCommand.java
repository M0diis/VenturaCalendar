package me.m0dii.venturacalendar.game.commands;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.DateCalculator;
import me.m0dii.venturacalendar.base.dateutils.RealTimeDate;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.base.dateutils.VenturaCalendarDate;
import me.m0dii.venturacalendar.base.events.CalendarOpenEvent;
import me.m0dii.venturacalendar.base.events.RealTimeCalendarOpenEvent;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.game.config.Messages;
import me.m0dii.venturacalendar.game.gui.Calendar;
import me.m0dii.venturacalendar.game.gui.RealTimeCalendar;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CalendarCommand {

    public CalendarCommand(CommandSender sender,
                           String[] args,
                           VenturaCalendar plugin) {
        if (sender instanceof Player pl) {
            TimeSystem timeSystem = plugin.getTimeConfig().getTimeSystem();

            if (args.length == 0) {
                if (!pl.hasPermission("venturacalendar.command.calendar")) {
                    Messenger.send(pl, Messages.NO_PERMISSION);

                    return;
                }

                World world = plugin.getTimeSystemWorld(pl);

                if (timeSystem.isRealTime()) {
                    RealTimeDate date = DateCalculator.realTimeNow();

                    RealTimeCalendar calendar = new RealTimeCalendar(date);

                    RealTimeCalendarOpenEvent openEvent = new RealTimeCalendarOpenEvent(calendar, calendar.getInventory(), pl);
                    Bukkit.getPluginManager().callEvent(openEvent);

                    if (!openEvent.isCancelled()) {
                        pl.openInventory(calendar.getInventory());
                    }

                    return;
                } else if (world == null) {
                    Messenger.log(Messenger.Level.WARN, "World '" + timeSystem.getWorldName() + "' was not found.");
                    Messenger.log(Messenger.Level.WARN, "Falling back to world name 'world'.");

                    world = Bukkit.getWorld("world");

                    if (world == null) {
                        Messenger.log(Messenger.Level.WARN, "Fall-back world named 'world' was not found.");

                        return;
                    }
                }

                VenturaCalendarDate venturaCalendarDate = DateCalculator.fromTicks(world.getFullTime(), timeSystem);
                VenturaCalendarDate creationVenturaCalendarDate = DateCalculator.fromTicks(world.getFullTime(), timeSystem);

                Calendar calendar = new Calendar(venturaCalendarDate, creationVenturaCalendarDate, plugin);

                CalendarOpenEvent openEvent = new CalendarOpenEvent(calendar, calendar.getInventory(), pl);
                Bukkit.getPluginManager().callEvent(openEvent);

                if (!openEvent.isCancelled()) {
                    pl.openInventory(calendar.getInventory());
                }

                return;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("realtime")) {
                if (!pl.hasPermission("venturacalendar.command.calendar.realtime")) {
                    Messenger.send(pl, Messages.NO_PERMISSION);

                    return;
                }

                RealTimeDate date = DateCalculator.realTimeNow();

                RealTimeCalendar calendar = new RealTimeCalendar(date);

                RealTimeCalendarOpenEvent openEvent = new RealTimeCalendarOpenEvent(calendar, calendar.getInventory(), pl);
                Bukkit.getPluginManager().callEvent(openEvent);

                if (!openEvent.isCancelled()) {
                    pl.openInventory(calendar.getInventory());
                }

                return;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("game")) {
                if (!pl.hasPermission("venturacalendar.command.calendar.game")) {
                    Messenger.send(pl, Messages.NO_PERMISSION);

                    return;
                }

                World world = plugin.getTimeSystemWorld(pl);

                if (world == null) {
                    Messenger.log(Messenger.Level.WARN, "World '" + timeSystem.getWorldName() + "' was not found.");
                    Messenger.log(Messenger.Level.WARN, "Falling back to world name 'world'.");

                    world = Bukkit.getWorld("world");

                    if (world == null) {
                        Messenger.log(Messenger.Level.WARN, "Fall-back world named 'world' was not found.");

                        return;
                    }
                }

                VenturaCalendarDate venturaCalendarDate = DateCalculator.fromTicks(world.getFullTime(), timeSystem);
                VenturaCalendarDate creationVenturaCalendarDate = DateCalculator.fromTicks(world.getFullTime(), timeSystem);

                Calendar calendar = new Calendar(venturaCalendarDate, creationVenturaCalendarDate, plugin);

                CalendarOpenEvent openEvent = new CalendarOpenEvent(calendar, calendar.getInventory(), pl);
                Bukkit.getPluginManager().callEvent(openEvent);

                if (!openEvent.isCancelled()) {
                    pl.openInventory(calendar.getInventory());
                }

                return;
            }

            Messenger.send(pl, Messages.UNKNOWN_COMMAND);
        } else {
            Messenger.send(sender, Messages.NOT_PLAYER);
        }
    }
}
