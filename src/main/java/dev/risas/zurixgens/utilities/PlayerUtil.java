package dev.risas.zurixgens.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */

@UtilityClass
public class PlayerUtil {

    public void decrementItem(Player player, ItemStack itemStack) {
        int amount = itemStack.getAmount();

        if (amount > 1) {
            itemStack.setAmount(amount - 1);
        }
        else {
            player.getInventory().removeItem(itemStack);
        }
    }
}
