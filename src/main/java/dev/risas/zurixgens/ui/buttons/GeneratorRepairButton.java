package dev.risas.zurixgens.ui.buttons;

import dev.risas.zurixgens.controllers.EconomyController;
import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.models.generator.Generator;
import dev.risas.zurixgens.models.generator.GeneratorPlayer;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.CurrencyUtil;
import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.ItemBuilder;
import dev.risas.zurixgens.utilities.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorRepairButton extends Button {

    private final GeneratorPlayer generatorPlayer;
    private final FileConfig languageFile;
    private final GeneratorController generatorController;
    private final EconomyController economyController;

    public GeneratorRepairButton(
            GeneratorPlayer generatorPlayer,
            FileConfig languageFile,
            GeneratorController generatorController,
            EconomyController economyController) {
        this.languageFile = languageFile;
        this.generatorPlayer = generatorPlayer;
        this.generatorController = generatorController;
        this.economyController = economyController;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        Generator generator = generatorPlayer.getGenerator();
        return new ItemBuilder(generator.getItem().getType())
                .setDisplayName(generator.getDisplayName())
                .setLore(
                        "&8Este generator está roto.",
                        "",
                        "&fReparación&7: &a$" + CurrencyUtil.format(generator.getRepair()),
                        "",
                        "&eHaz clic para reparar este generator."
                )
                .setEnchanted(true)
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        int repairCost = generatorPlayer.getGenerator().getRepair();

        if (economyController.hasNotBalance(player, repairCost)) {
            playFailure(player);

            for (String message : languageFile.getStringList("generator-message.repair.not-balance")) {
                ChatUtil.sendMessage(player, message
                        .replace("%repair-cost%", CurrencyUtil.format(repairCost)));
            }
            return;
        }

        playNeutral(player);
        player.closeInventory();

        generatorPlayer.repair();
        generatorController.saveGeneratorPlayer(generatorPlayer, false);

        economyController.removeBalance(player, repairCost);
        ChatUtil.sendMessage(player, languageFile.getString("generator-message.repair.repaired"));
    }

    @Override
    public boolean isCloseableAfterClick() {
        return false;
    }
}
