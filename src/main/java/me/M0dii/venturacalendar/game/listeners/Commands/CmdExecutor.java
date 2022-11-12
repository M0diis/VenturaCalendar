package me.m0dii.venturacalendar.game.listeners.commands;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdExecutor implements CommandExecutor, TabCompleter {
    final TimeSystem timeSystem;

    final VenturaCalendar plugin;

    public CmdExecutor(VenturaCalendar plugin) {
        this.plugin = plugin;

        this.timeSystem = plugin.getTimeConfig().getTimeSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("calendar")) {
            new CalendarCommand(sender, command, label, args, plugin);
        }

        if (command.getName().equalsIgnoreCase("venturacalendar")) {
            new VenturaCalendarCommand(sender, args, plugin);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        List<String> completes = new ArrayList<>();

        String name = command.getName().toLowerCase();

        if (name.equals("venturacalendar") || name.equals("vc")) {
            if (args.length == 1) {
                addCompletesMatching(completes,
                        args[0],
                        "reload",
                        "set",
                        "add",
                        "subtract",
                        "fastforward",
                        "rewind");
            }

            if (args.length == 2 && args[0].equals("reload")) {
                return completes;
            }

            if (args.length == 2 && (args[0].equals("fastforward")
                    || args[0].equals("ff")
                    || args[0].equals("rewind")
                    || args[0].equals("rew"))) {
                return Collections.emptyList();
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    addCompletesMatching(completes, args[1], "startyear", "date", "worldticks", "offset");
                }
                else {
                    completes.add("seconds");
                    completes.add("minutes");
                    completes.add("hours");
                    completes.add("days");
                    completes.add("weeks");
                }
            }

            if (args.length == 3) {
                if(args[1].equalsIgnoreCase("offset")) {
                    addCompletesMatching(completes, args[2], "seconds", "minutes", "hours", "days", "weeks", "years");
                }

                if (args[2].equalsIgnoreCase("date")) {
                    completes.add("YYYY/MM/DD");
                }
            }
        }

        return completes;
    }

    private void addCompletesMatching(List<String> completes, String arg, String... args) {
        for(String s : args) {
            if (s.toLowerCase().contains(arg.toLowerCase())) {
                completes.add(s);
            }
        }
    }
}
