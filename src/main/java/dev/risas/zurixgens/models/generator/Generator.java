package dev.risas.zurixgens.models.generator;

import dev.risas.zurixgens.controllers.GeneratorController;
import dev.risas.zurixgens.utilities.CurrencyUtil;
import dev.risas.zurixgens.utilities.ItemBuilder;
import dev.risas.zurixgens.utilities.PersistentDataUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Risas
 * @date 15-06-2025
 * @discord https://risas.me/discord
 */

@Getter
public class Generator {

    private final String id;
    private final int time, upgrade, price, repair;
    private final ItemStack item;
    private final Map<String, GeneratorDrop> drops;

    public Generator(String id, ConfigurationSection section) {
        this.id = id;
        this.time = section.getInt("time");
        this.upgrade = section.getInt("upgrade");
        this.price = section.getInt("price");
        this.repair = section.getInt("repair");
        this.item = new ItemBuilder(section.getString("item.material"))
                .setSkullOwner(section.getString("item.head"))
                .setDisplayName(section.getString("item.displayname"))
                .setLore(section.getStringList("item.lore"))
                .setEnchanted(true)
                .addPersistentData(PersistentDataUtil.GENERATOR_ID, PersistentDataType.STRING, id)
                .build();
        this.drops = new HashMap<>();

        ConfigurationSection dropsSection = section.getConfigurationSection("drops");

        if (dropsSection != null) {
            for (String dropId : dropsSection.getKeys(false)) {
                this.drops.put(dropId, new GeneratorDrop(dropId, Objects.requireNonNull(dropsSection.getConfigurationSection(dropId))));
            }
        }
    }

    public ItemStack getItem(GeneratorController generatorController, int amount) {
        Generator nextGenerator = generatorController.getNextGenerator(id);

        ItemStack itemStack = item.clone();
        return new ItemBuilder(itemStack)
                .setAmount(amount)
                .setLore(itemStack.getItemMeta().getLore().stream()
                        .map(line -> line
                                .replace("%time%", String.valueOf(time))
                                .replace("%upgrade%", getNextUpgradeFormatted(nextGenerator))
                                .replace("%price%", CurrencyUtil.format(price))
                                .replace("%repair%", CurrencyUtil.format(repair)))
                        .collect(Collectors.toList()))
                .build();
    }

    public void give(Player player, GeneratorController generatorController, int amount) {
        player.getInventory().addItem(getItem(generatorController, amount));
    }

    public String getDisplayName() {
        return item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : id;
    }

    public String getUpdateFormatted() {
        return CurrencyUtil.format(upgrade);
    }

    public String getNextUpgradeFormatted(Generator nextGenerator) {
        return nextGenerator == null ? "&c&lMAXIMO" : "$" + nextGenerator.getUpdateFormatted();
    }

    public GeneratorDrop getRandomGeneratorDrop() {
        Collection<GeneratorDrop> drops = this.drops.values();
        double totalWeight = drops.stream().mapToDouble(GeneratorDrop::getSpawnChance).sum();

        if (totalWeight <= 0) return null;

        double random = Math.random() * totalWeight;
        double current = 0;

        for (GeneratorDrop drop : drops) {
            current += drop.getSpawnChance();

            if (random <= current) {
                return drop;
            }
        }

        return null;
    }
}
