package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.FileConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class StatisticCommand implements CommandExecutor {

    private final FileConfig languageFile;
    private final UserController userController;

    public StatisticCommand(FileConfig languageFile, UserController userController) {
        this.languageFile = languageFile;
        this.userController = userController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendMessage(sender, "&cYou must be a player to use this command.");
            return true;
        }

        User user = userController.getUser(player.getUniqueId());
        String totalItems = String.valueOf(user.getTotalItems()),
                totalEnchantedItems = String.valueOf(user.getTotalEnchantedItems()),
                totalGlowItems = String.valueOf(user.getTotalGlowItems()),
                totalPurchases = String.valueOf(user.getTotalPurchases());

        for (String message : languageFile.getStringList("statistic-message")) {
            ChatUtil.sendMessage(player, message
                    .replace("%total-items%", totalItems)
                    .replace("%total-enchanted-items%", totalEnchantedItems)
                    .replace("%total-glow-items%", totalGlowItems)
                    .replace("%total-purchases%", totalPurchases));
        }
        return false;
    }
}
