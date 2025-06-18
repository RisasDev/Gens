package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.models.generator.Generator;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorListCommand extends SubCommand {

    private final GeneratorController generatorController;

    public GeneratorListCommand(GeneratorController generatorController) {
        super("zurixgens.command.generator.list", "Lista todos los generadores disponibles.");
        this.generatorController = generatorController;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        ChatUtil.sendMessage(sender, ChatUtil.NORMAL_LINE);
        ChatUtil.sendMessage(sender, "&6&lGenerators");
        ChatUtil.sendMessage(sender, "");

        if (generatorController.getGenerators().isEmpty()) {
            ChatUtil.sendMessage(sender, "&cNo hay generadores disponibles.");
        }
        else {
            for (Generator generator : generatorController.getGenerators()) {
                ChatUtil.sendMessage(sender, " &7● &f" + generator.getId() + " - " + generator.getDisplayName());
            }
        }

        ChatUtil.sendMessage(sender, ChatUtil.NORMAL_LINE);
    }
}
