package me.m0dii.venturacalendar.game.commands;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.Month;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.config.Messages;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class VenturaCalendarCommand {
    private final List<BukkitRunnable> tasks = new ArrayList<>();

    public VenturaCalendarCommand(CommandSender sender, String[] args, VenturaCalendar plugin) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("venturacalendar.command.reload")) {
                    Messenger.send(sender, Messages.NO_PERMISSION);

                    return;
                }

                plugin.getTimeConfig().reloadConfig();
                plugin.getCalendarConfig().reloadConfig();
                plugin.getBaseConfig().reloadConfig();
                plugin.getEventConfig().reloadConfig();

                Messenger.send(sender, Messages.CONFIG_RELOADED);

                return;
            } else if (alias(args[0], "help, info")) {
                if (!sender.hasPermission("venturacalendar.command.help")) {
                    Messenger.send(sender, Messages.NO_PERMISSION);

                    return;
                }

                Messenger.send(sender, Messages.HELP);

                return;
            } else {
                Messenger.send(sender, Messages.HELP);
            }
        }

        if (args.length > 1 && sender instanceof Player p) {
            if (alias(args[0], "fastforward, ff, rewind, rew") && alias(args[1], "cancel, stop")) {
                if (!sender.hasPermission("venturacalendar.command.fastforward")) {
                    Messenger.send(sender, Messages.NO_PERMISSION);
                    return;
                }

                for (BukkitRunnable task : tasks) {
                    task.cancel();
                }

                return;
            }

            if (alias(args[0], "fastforward, ff, rewind, rew")) {
                if (!sender.hasPermission("venturacalendar.command.fastforward")) {
                    Messenger.send(sender, Messages.NO_PERMISSION);
                    return;
                }

                TimeSystem timeSystem = plugin.getTimeConfig().getTimeSystem();

                if (timeSystem.isRealTime()) {
                    Messenger.send(sender, Messages.REAL_TIME_SET);

                    return;
                }

                long ticksToAdd = 0;

                for (int i = 1; i < args.length; i++) {
                    ticksToAdd += Utils.getTicksFromTime(args[i]);
                }

                World w = p.getWorld();

                final long[] total = {ticksToAdd};

                boolean add = alias(args[0], "fastforward, ff");

                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        long toAdd = 50;

                        if (total[0] < toAdd) {
                            toAdd = total[0];
                        }

                        if (add) {
                            w.setFullTime(w.getFullTime() + toAdd);
                        } else {
                            w.setFullTime(w.getFullTime() - toAdd);
                        }

                        total[0] -= toAdd;

                        if (total[0] <= 0) {
                            this.cancel();
                        }

                        if (toAdd % 100 == 0) {
                            toAdd += 10;
                        }
                    }
                };

                runnable.runTaskTimer(plugin, 0, 1);

                tasks.add(runnable);

                return;
            }
        }

        if (args.length >= 3 && sender instanceof Player p) {
            if (!sender.hasPermission("venturacalendar.command.changetime")) {
                Messenger.send(sender, Messages.NO_PERMISSION);

                return;
            }

            if (alias(args[0], "set")) {
                if (alias(args[1], "startyear, startingyear") && args.length == 3) {
                    TimeSystem timeSystem = plugin.getTimeConfig().getTimeSystem();

                    if (timeSystem.isRealTime()) {
                        Messenger.send(sender, Messages.REAL_TIME_SET);

                        return;
                    }

                    try {
                        int startYear = Integer.parseInt(args[2]);

                        plugin.getTimeConfig().set("main-time-system.starting-year", startYear);

                        plugin.getTimeConfig().reloadConfig();
                        plugin.getBaseConfig().reloadConfig();

                        Messenger.send(p, "&aSuccessfully set starting year to " + startYear);
                    } catch (NumberFormatException ex) {
                        Messenger.send(p, "&aInvalid number format.");
                        Messenger.log(Messenger.Level.DEBUG, ex);
                    }
                }

                if (alias(args[1], "offset") && args.length == 4) {
                    try {
                        int value = Integer.parseInt(args[3]);

                        String path = "main-time-system.real-time.offsets";

                        if (alias(args[2], "sec, second, seconds")) {
                            plugin.getTimeConfig().set(path + ".second", value);
                        } else if (alias(args[2], "min, minute, minutes")) {
                            plugin.getTimeConfig().set(path + ".minute", value);
                        } else if (alias(args[2], "h, hour, hours")) {
                            plugin.getTimeConfig().set(path + ".hour", value);
                        } else if (alias(args[2], "d, day, days")) {
                            plugin.getTimeConfig().set(path + ".day", value);
                        } else if (alias(args[2], "w, week, weeks")) {
                            plugin.getTimeConfig().set(path + ".week", value);
                        } else if (alias(args[2], "mon, month, months")) {
                            plugin.getTimeConfig().set(path + ".month", value);
                        } else if (alias(args[2], "y, year, years")) {
                            plugin.getTimeConfig().set(path + ".year", value);
                        } else {
                            Messenger.send(p, "&aInvalid offset type.");
                            return;
                        }

                        plugin.getTimeConfig().reloadConfig();
                        plugin.getBaseConfig().reloadConfig();

                        Messenger.send(p, "&aSuccessfully set &2'" + args[2] + "'&a offset to &2" + value);
                    } catch (NumberFormatException ex) {
                        Messenger.send(p, "&cInvalid number format.");
                        Messenger.log(Messenger.Level.DEBUG, ex);
                    }
                }

                if (alias(args[1], "worldticks, fulltime") && args.length == 3) {
                    TimeSystem timeSystem = plugin.getTimeConfig().getTimeSystem();

                    if (timeSystem.isRealTime()) {
                        Messenger.send(sender, Messages.REAL_TIME_SET);

                        return;
                    }

                    try {
                        long worldTicks = Long.parseLong(args[2]);

                        p.getWorld().setFullTime(worldTicks);

                        Messenger.send(p, "&aSuccessfully set world ticks to " + worldTicks);
                    } catch (NumberFormatException ex) {
                        Messenger.send(p, "&aInvalid number format.");
                        Messenger.log(Messenger.Level.DEBUG, ex);
                    }
                }

                if (alias(args[1], "date") && args.length == 3) {
                    TimeSystem timeSystem = plugin.getTimeConfig().getTimeSystem();

                    if (timeSystem.isRealTime()) {
                        Messenger.send(sender, Messages.REAL_TIME_SET);

                        return;
                    }

                    try {
                        String[] values = args[2].split("/");

                        if (values.length < 2) {
                            return;
                        }

                        String year = values[0];
                        String month = values[1];
                        String day = values[2];

                        TimeSystem ts = plugin.getTimeConfig().getTimeSystem();

                        int maxMonth = ts.getMonths().size() - 1;

                        int m = 1;

                        try {
                            m = Integer.parseInt(month) - 1;
                        } catch (NumberFormatException ex) {
                            Messenger.send(p, "&cIllegal month number format.");
                            Messenger.log(Messenger.Level.DEBUG, ex);
                        }

                        if (m > maxMonth || m < 0) {
                            Messenger.send(p, "&cSpecified month does not exist.");

                            return;
                        }

                        Month mn = ts.getMonths().get(m);

                        int maxDays = (int) mn.getDays();

                        int d = 1;

                        try {
                            d = Integer.parseInt(day);
                        } catch (NumberFormatException ex) {
                            Messenger.send(p, "&cIllegal day number format.");
                            Messenger.log(Messenger.Level.DEBUG, ex);
                        }

                        if (d > maxDays || d <= 0) {
                            Messenger.send(p, "&cSpecified day in month " + mn.getName() + " does not exist.");

                            return;
                        }

                        plugin.getTimeConfig().set("main-time-system.starting-year", year);
                        plugin.getTimeConfig().set("main-time-system.month-offset", m);
                        plugin.getTimeConfig().set("main-time-system.day-offset", d - 1);

                        Messenger.send(p, "&aDate has been set to " + year + "/" + month + "/" + day);

                        p.getWorld().setFullTime(0);

                        plugin.getTimeConfig().reloadConfig();
                        plugin.getBaseConfig().reloadConfig();

                    } catch (NumberFormatException ex) {
                        Messenger.send(p, "&aIllegal number format.");
                        Messenger.log(Messenger.Level.DEBUG, ex);
                    }
                }
            }

            if (alias(args[0], "add, subtract")) {
                TimeSystem timeSystem = plugin.getTimeConfig().getTimeSystem();

                if (timeSystem.isRealTime()) {
                    Messenger.send(sender, Messages.REAL_TIME_SET);

                    return;
                }

                boolean sub = args[0].equalsIgnoreCase("subtract");

                if (alias(args[1], "s, sec, second, seconds"))
                    fastForwards(args[2], p, 20L, " second.", " hours.", sub);

                if (alias(args[1], "m, min, minute, minutes"))
                    fastForwards(args[2], p, 1200L, " second.", " hours.", sub);

                if (alias(args[1], "h, hour, hours"))
                    fastForwards(args[2], p, 1000L, " hour.", " hours.", sub);

                if (alias(args[1], "d, day, days"))
                    fastForwards(args[2], p, 24000L, " day.", " days.", sub);

                if (alias(args[1], "w, week, weeks"))
                    fastForwards(args[2], p, 168000L, " week.", " weeks.", sub);
            }
        } else Messenger.send(sender, Messages.HELP);
    }

    private void fastForwards(String amt, Player p, long oneAmount, String s, String s2, boolean subtract) {
        try {
            amt = amt.replaceAll("\\D", "");

            if (amt.isEmpty())
                amt = "0";

            if (subtract)
                amt = '-' + amt;

            int amount = Integer.parseInt(amt);

            long total = oneAmount * amount;

            World w = p.getWorld();

            if (w.getFullTime() + total > 0) {
                w.setFullTime(w.getFullTime() + total);

                Messenger.send(p, "&aFast-forwarded the time by " + amount + (amt.charAt(0) == '1' ? s : s2));
            } else Messenger.send(p, "&aWorld time can not go below 0 days.");
        } catch (NumberFormatException ex) {
            Messenger.send(p, "&cProvided amount is not valid.");
        }
    }

    private boolean alias(String cmd, String names) {
        String[] split = names.split(", ");

        for (String s : split) {
            if (cmd.equalsIgnoreCase(s))
                return true;
        }

        return false;
    }
}
