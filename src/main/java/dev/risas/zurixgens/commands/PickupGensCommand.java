package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.generator.GeneratorPlayer;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Risas
 * @date 20-06-2025
 * @discord https://risas.me/discord
 */
public class PickupGensCommand implements CommandExecutor {

    private final UserController userController;
    private final GeneratorController generatorController;

    public PickupGensCommand(UserController userController, GeneratorController generatorController) {
        this.userController = userController;
        this.generatorController = generatorController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendMessage(sender, "&cYou must be a player to use this command.");
            return true;
        }

        User user = userController.getUser(player.getUniqueId());
        List<GeneratorPlayer> generators = user.getGenerators();

        if (generators.isEmpty()) {
            ChatUtil.sendMessage(sender, "&cNo tienes generadores para recoger.");
            return true;
        }

        for (GeneratorPlayer generator : generators) {
            if (generator.isBroken()) {
                ChatUtil.sendMessage(sender, "&cNo puedes recoger todos los generadores ya que algunos están rotos.");
                return true;
            }
        }

        generatorController.removeAllGeneratorPlayer(player, generators);

        user.stopGeneratorTask();
        user.getGenerators().clear();

        userController.saveUser(user);

        ChatUtil.sendMessage(sender, "&aHas recogido todos tus generadores.");
        return false;
    }
}
