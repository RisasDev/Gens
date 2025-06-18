package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.ChatUtil;
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

    private final UserController userController;

    public StatisticCommand(UserController userController) {
        this.userController = userController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendMessage(sender, "&cYou must be a player to use this command.");
            return true;
        }

        User user = userController.getUser(player.getUniqueId());

        ChatUtil.sendMessage(sender, new String[]{
                ChatUtil.NORMAL_LINE,
                "&6&lStatistics",
                "",
                " &7● &fTotal Items: &e" + user.getTotalItems(),
                " &7● &fTotal Enchanted Items: &e" + user.getTotalEnchantedItems(),
                " &7● &fTotal Glow Items: &e" + user.getTotalGlowItems(),
                " &7● &fTotal Purchases: &e" + user.getTotalPurchases(),
                ChatUtil.NORMAL_LINE
        });
        return false;
    }
}
