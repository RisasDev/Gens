package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.EconomyController;
import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.CurrencyUtil;
import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.PersistentDataUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class SellCommand implements CommandExecutor {

    private final FileConfig configFile, languageFile;
    private final UserController userController;
    private final EconomyController economyController;
    private final EventController eventController;

    public SellCommand(
            FileConfig configFile,
            FileConfig languageFile,
            UserController userController,
            EconomyController economyController,
            EventController eventController) {
        this.configFile = configFile;
        this.languageFile = languageFile;
        this.userController = userController;
        this.economyController = economyController;
        this.eventController = eventController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendMessage(sender, "&cYou must be a player to use this command.");
            return true;
        }

        double totalSell = 0;

        int glowMultiplier = configFile.getInt("generator-system.drop-multiplier.glow"),
            enchantedMultiplier = configFile.getInt("generator-system.drop-multiplier.enchanted");

        for (ItemStack itemStack : player.getInventory().getStorageContents()) {
            if (itemStack == null) continue;

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) continue;

            PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
            if (!persistentDataContainer.has(PersistentDataUtil.GENERATOR_DROP_ID, PersistentDataType.STRING)) continue;

            boolean isEnchanted = persistentDataContainer.has(PersistentDataUtil.GENERATOR_DROP_ENCHANTED, PersistentDataType.STRING),
                    isGlow = persistentDataContainer.has(PersistentDataUtil.GENERATOR_DROP_GLOW, PersistentDataType.STRING);

            int price = persistentDataContainer.get(PersistentDataUtil.GENERATOR_DROP_PRICE, PersistentDataType.INTEGER) * eventController.getDoubleValue();

            if (isEnchanted && isGlow) {
                price = price * (enchantedMultiplier + glowMultiplier);
            }
            else if (isEnchanted) {
                price = price * enchantedMultiplier;
            }
            else if (isGlow) {
                price = price * glowMultiplier;
            }

            int amount = itemStack.getAmount();

            totalSell += price * amount;

            player.getInventory().removeItem(itemStack);
        }

        if (totalSell == 0) {
            ChatUtil.sendMessage(sender, languageFile.getString("sell-message.nothing-to-sell"));
            return true;
        }

        User user = userController.getUser(player.getUniqueId());
        double multiplier = user.getMultiplier();

        if (multiplier > 0) {
            totalSell = totalSell * (1 + multiplier);
        }

        economyController.giveBalance(player, totalSell);
        ChatUtil.sendMessage(sender, languageFile.getString("sell-message.sold")
                .replace("%earned%", CurrencyUtil.format(totalSell)));
        return false;
    }
}
