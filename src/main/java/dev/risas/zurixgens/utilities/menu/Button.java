package dev.risas.zurixgens.utilities.menu;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    public abstract ItemStack getItemStack(Player player);

    public static Button getButton(ItemStack itemStack) {
        return (new Button() {
            public ItemStack getItemStack(Player player) {
                return itemStack.clone();
            }

            @Override
            public boolean isCloseableAfterClick() {
                return false;
            }
        });
    }

    public void playFailure(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,20F, 1F);
    }

    public void playNeutral(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,20F, 1F);
    }

    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {}

    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return false;
    }

    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }

    public boolean shouldShift(Player player, int slot, ClickType clickType) {
        return true;
    }

    public boolean isCloseableAfterClick() {
        return true;
    }
}