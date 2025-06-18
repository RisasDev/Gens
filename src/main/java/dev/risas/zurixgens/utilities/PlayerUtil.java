package dev.risas.zurixgens.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */

@UtilityClass
public class PlayerUtil {

    public void sendTitle(Player player, List<String> list) {
        player.sendTitle(ChatUtil.translate(list.get(0)), ChatUtil.translate(list.get(1)), 20, 70, 20);
    }

    public void sendAllTitle(List<String> list) {
        Bukkit.getOnlinePlayers().forEach(player -> sendTitle(player, list));
    }

    public void sendSound(Player player, String sound) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf(sound), 1F, 1F);
        }
        catch (Exception e) {
            Bukkit.getLogger().warning("Sound '" + sound + "' not found");
        }
    }

    public void sendAllSound(String sound) {
        try {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.playSound(player.getLocation(), Sound.valueOf(sound), 1F, 1F));
        }
        catch (Exception e) {
            Bukkit.getLogger().warning("Sound '" + sound + "' not found");
        }
    }

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
