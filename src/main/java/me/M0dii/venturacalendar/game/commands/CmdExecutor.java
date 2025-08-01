package me.m0dii.venturacalendar.game.commands;

import me.m0dii.venturacalendar.VenturaCalendar;
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

    private final VenturaCalendar plugin;

    public CmdExecutor(VenturaCalendar plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String @NotNull [] args) {
        if (command.getName().equalsIgnoreCase("calendar")) {
            new CalendarCommand(sender, args, plugin);
        }

        if (command.getName().equalsIgnoreCase("venturacalendar")) {
            new VenturaCalendarCommand(sender, args, plugin);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String @NotNull [] args) {
        List<String> completes = new ArrayList<>();

        String name = command.getName().toLowerCase();

        if (name.equals("calendar") || name.equalsIgnoreCase("cal")) {
            if (args.length == 1) {
                addCompletesMatching(completes, args[0],
                        "realtime",
                        "game");
            }
        }

        if (name.equals("venturacalendar") || name.equals("vc")) {
            if (args.length == 1) {
                addCompletesMatching(completes, args[0],
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
                    addCompletesMatching(completes, args[1],
                            "startyear",
                            "date",
                            "worldticks",
                            "offset");
                } else {
                    addCompletesMatching(completes, args[1],
                            "seconds",
                            "minutes",
                            "hours",
                            "days",
                            "weeks");
                }
            }

            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("offset")) {
                    addCompletesMatching(completes, args[2],
                            "seconds",
                            "minutes",
                            "hours",
                            "days",
                            "weeks",
                            "years");
                }

                if (args[2].equalsIgnoreCase("date")) {
                    completes.add("YYYY/MM/DD");
                }
            }
        }

        return completes;
    }

    private void addCompletesMatching(List<String> completes, String arg, String... args) {
        for (String s : args) {
            if (s.toLowerCase().contains(arg.toLowerCase())) {
                completes.add(s);
            }
        }
    }
}
