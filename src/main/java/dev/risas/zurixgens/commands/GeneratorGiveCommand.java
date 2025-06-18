package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.models.generator.Generator;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorGiveCommand extends SubCommand {

    private final GeneratorController generatorController;

    public GeneratorGiveCommand(GeneratorController generatorController) {
        super(List.of("<player>", "<generator>", "[amount]"), "zurixgens.command.generator.give","Dar un generador a un jugador.");
        this.generatorController = generatorController;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            ChatUtil.sendMessage(sender, "&cUsage: /" + label + " give <player> <generator> [amount]");
            return;
        }

        String playerName = args[1];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            ChatUtil.sendMessage(sender, "&cPlayer '" + playerName + "' not found.");
            return;
        }

        String generatorName = args[2];
        Generator generator = generatorController.getGenerator(generatorName);

        if (generator == null) {
            ChatUtil.sendMessage(sender, "&cGenerator '" + generatorName + "' not found.");
            return;
        }

        int amount = args.length > 3 ? Integer.parseInt(args[3]) : 1;

        if (amount <= 0) {
            ChatUtil.sendMessage(sender, "&cAmount must be greater than 0.");
            return;
        }

        generator.give(player, generatorController, amount);
        ChatUtil.sendMessage(sender, "&fLe has dado &e" + amount + " &f" + generator.getDisplayName() + " &fa &6" + player.getName() + "&f.");
    }
}
