package dev.risas.zurixgens.models.generator;

import dev.risas.zurixgens.controllers.EventController;
import dev.risas.zurixgens.utilities.CurrencyUtil;
import dev.risas.zurixgens.utilities.ItemBuilder;
import dev.risas.zurixgens.utilities.PersistentDataUtil;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.stream.Collectors;

/**
 * @author Risas
 * @date 15-06-2025
 * @discord https://risas.me/discord
 */

@Getter
public class GeneratorDrop {

    private final String id;
    private final ItemStack item;
    private final int price;
    private final double spawnChance, enchantedChance, glowChance;
    private final Color glowColor;

    public GeneratorDrop(String id, ConfigurationSection section) {
        this.id = id;
        this.price = section.getInt("price");
        this.item = new ItemBuilder(section.getString("item.material"))
                .setSkullOwner(section.getString("item.head"))
                .setDisplayName(section.getString("item.displayname"))
                .setLore(section.getStringList("item.lore"))
                .setEnchanted(section.getBoolean("item.enchanted"))
                .addPersistentData(PersistentDataUtil.GENERATOR_DROP_ID, PersistentDataType.STRING, id)
                .addPersistentData(PersistentDataUtil.GENERATOR_DROP_PRICE, PersistentDataType.INTEGER, price)
                .build();
        this.spawnChance = section.getDouble("chance.spawn");
        this.enchantedChance = section.getDouble("chance.enchanted");
        this.glowChance = section.getDouble("chance.glow");
        this.glowColor = Color.fromRGB(
                section.getInt("glow-color.red"),
                section.getInt("glow-color.green"),
                section.getInt("glow-color.blue"));
    }

    public ItemStack getDropItem() {
        ItemStack itemStack = item.clone();
        return new ItemBuilder(itemStack)
                .setLore(itemStack.getItemMeta().getLore().stream()
                        .map(line -> line
                                .replace("%price%", CurrencyUtil.format(price)))
                        .collect(Collectors.toList()))
                .build();
    }

    public double getGlowChance(EventController eventController) {
        return glowChance * eventController.getDoubleGlowChance();
    }

    public double getEnchantedChance(EventController eventController) {
        return enchantedChance * eventController.getDoubleEnchantedChance();
    }
}
