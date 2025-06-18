package dev.risas.zurixgens.commands;

import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.models.generator.GeneratorMultiplierItem;
import dev.risas.zurixgens.models.generator.GeneratorSlotItem;
import dev.risas.zurixgens.utilities.ChatUtil;
import dev.risas.zurixgens.utilities.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */
public class GeneratorItemCommand extends SubCommand {

    private final GeneratorController generatorController;

    public GeneratorItemCommand(GeneratorController generatorController) {
        super(List.of("give", "<player>", "<slot|multiplier>", "<id>"),
                "zurixgens.command.generator.item",
              "Dar un item especial a un jugador (slot o multiplicador)."
        );
        this.generatorController = generatorController;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 5) {
            ChatUtil.sendMessage(sender, "&cUsage: /" + label + " item give <player> <slot|multiplier> <id> [amount]");
            return;
        }

        String playerName = args[2];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            ChatUtil.sendMessage(sender, "&cPlayer '" + playerName + "' not found.");
            return;
        }

        String option = args[3].toLowerCase();
        String identifier = args[4].toLowerCase();
        int amount = args.length > 5 ? Integer.parseInt(args[5]) : 1;

        ItemStack itemStack;

        switch (option) {
            case "slot" -> {
                GeneratorSlotItem slotItem = generatorController.getGeneratorSlotItem(identifier);

                if (slotItem == null) {
                    ChatUtil.sendMessage(sender, "&cSlot item '" + identifier + "' not found.");
                    return;
                }
                
                itemStack = slotItem.getItemStack().clone();
            }
            case "multiplier" -> {
                GeneratorMultiplierItem multiplierItem = generatorController.getGeneratorMultiplierItem(identifier);

                if (multiplierItem == null) {
                    ChatUtil.sendMessage(sender, "&cMultiplier item '" + identifier + "' not found.");
                    return;
                }

                itemStack = multiplierItem.getItemStack().clone();
            }
            default -> {
                ChatUtil.sendMessage(sender, "&cOption must be 'slot' or 'multiplier'.");
                return;
            }
        }

        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(itemStack);
        }

        ChatUtil.sendMessage(sender, "&6&lGENERATORS &8» &fLe has dado &ex" + amount + " &f"
                + itemStack.getItemMeta().getDisplayName() + " &fa &6" + player.getName() + "&f.");
    }
}
