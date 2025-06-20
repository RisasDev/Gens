package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.controllers.EconomyController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.ui.GeneratorMenu;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.command.SubCommand;
import dev.risas.zurixgens.utilities.command.SubCommandHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorCommand implements CommandExecutor, TabCompleter {

    private final FileConfig languageFile, menusFile;
    private final UserController userController;
    private final GeneratorController generatorController;
    private final EconomyController economyController;

    private final Map<String, SubCommand> subCommands;

    public GeneratorCommand(
            ZurixGens plugin,
            FileConfig languageFile,
            FileConfig menusFile,
            UserController userController,
            GeneratorController generatorController,
            EconomyController economyController) {
        this.subCommands = SubCommandHelper.of(
                Map.entry("give", new GeneratorGiveCommand(generatorController)),
                Map.entry("list", new GeneratorListCommand(generatorController)),
                Map.entry("item", new GeneratorItemCommand(generatorController)),
                Map.entry("reload", new GeneratorReloadCommand(plugin))
        );
        this.languageFile = languageFile;
        this.menusFile = menusFile;
        this.userController = userController;
        this.generatorController = generatorController;
        this.economyController = economyController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                GeneratorMenu menu = new GeneratorMenu(player, languageFile, menusFile, userController, generatorController, economyController);
                menu.open();
                return true;
            }

            ChatUtil.sendMessage(sender, SubCommandHelper.getSubCommandFormat(label, subCommands, "Generator Commands"));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equalsIgnoreCase("help")
                && sender.hasPermission("zurixgens.command.generator.help")) {
            ChatUtil.sendMessage(sender, SubCommandHelper.getSubCommandFormat(label, subCommands, "Generator Commands"));
            return true;
        }

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
        if (args.length == 1 && sender.hasPermission("zurixgens.command.generator")) {
            return Stream.concat(Stream.of("help"), subCommands.keySet().stream())
                    .filter(sub -> sub.toLowerCase().startsWith(args[0].toLowerCase()))
                    .sorted()
                    .toList();
        }
        return Collections.emptyList();
    }
}
