package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.ZurixGens;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorReloadCommand extends SubCommand {

    private final ZurixGens plugin;

    public GeneratorReloadCommand(ZurixGens plugin) {
        super("zurixgens.command.generator.reload", "Recarga la configuración del plugin.");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        plugin.onReload();
        ChatUtil.sendMessage(sender, "&aZurixGens ha sido recargado con éxito.");
    }
}
