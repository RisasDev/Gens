package dev.risas.zurixgens.ui.buttons;

import dev.risas.zurixgens.controllers.EconomyController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.generator.Generator;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.CurrencyUtil;
import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.ItemBuilder;
import dev.risas.zurixgens.utilities.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorButton extends Button {

    private final Generator generator;
    private final FileConfig languageFile;
    private final UserController userController;
    private final GeneratorController generatorController;
    private final EconomyController economyController;

    public GeneratorButton(
            Generator generator,
            FileConfig languageFile,
            UserController userController,
            GeneratorController generatorController,
            EconomyController economyController) {
        this.languageFile = languageFile;
        this.generator = generator;
        this.userController = userController;
        this.generatorController = generatorController;
        this.economyController = economyController;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        ItemStack itemStack = generator.getItem(generatorController, 1).clone();
        List<String> lore = itemStack.getItemMeta().getLore();

        lore.add("");
        lore.add("&eHaz clic para comprar este generador.");

        return new ItemBuilder(itemStack)
                .setLore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        int price = generator.getPrice();

        if (economyController.hasNotBalance(player, price)) {
            playFailure(player);
            ChatUtil.sendMessage(player, languageFile.getString("generator-message.purchase.not-balance"));
            return;
        }

        playNeutral(player);

        economyController.removeBalance(player, price);
        generator.give(player, generatorController, 1);

        User user = userController.getUser(player.getUniqueId());
        user.addTotalPurchases(1);

        userController.saveUser(user);

        ChatUtil.sendMessage(player, languageFile.getString("generator-message.purchase.bought")
                .replace("%generator-displayname%", generator.getDisplayName())
                .replace("%generator-price%", CurrencyUtil.format(generator.getPrice())));
    }

    @Override
    public boolean isCloseableAfterClick() {
        return false;
    }
}
