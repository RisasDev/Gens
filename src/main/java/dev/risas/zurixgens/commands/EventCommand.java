package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.command.SubCommand;
import dev.risas.zurixgens.utilities.command.SubCommandHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class EventCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands;

    public EventCommand(EventController eventController) {
        this.subCommands = SubCommandHelper.of(
                Map.entry("start", new EventStartCommand(eventController)),
                Map.entry("stop", new EventStopCommand(eventController)),
                Map.entry("list", new EventListCommand())
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            ChatUtil.sendMessage(sender, SubCommandHelper.getSubCommandFormat(label, subCommands, "Event Commands"));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        SubCommand subCommandModel = subCommands.get(subCommand);

        if (subCommandModel == null) {
            ChatUtil.sendMessage(sender, "&cCommand '" + subCommand + "' not found.");
            return true;
        }

        if (!subCommandModel.hasPermission(sender)) {
            ChatUtil.sendMessage(sender, "&cYou do not have permission to use this command.");
            return true;
        }

        if (subCommandModel.isPlayerOnly() && !(sender instanceof Player)) {
            ChatUtil.sendMessage(sender, "&cYou must be a player to use this command.");
            return true;
        }

        subCommandModel.execute(sender, label, args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return args.length == 1 ? subCommands.keySet().stream().toList() : null;
    }
}
