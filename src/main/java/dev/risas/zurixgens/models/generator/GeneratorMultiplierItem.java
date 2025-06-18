package dev.risas.zurixgens.models.generator;

import dev.risas.zurixgens.controllers.UserController;
import dev.risas.zurixgens.models.user.User;
import dev.risas.zurixgens.utilities.ItemBuilder;
import dev.risas.zurixgens.utilities.PersistentDataUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author Risas
 * @date 17-06-2025
 * @discord https://risas.me/discord
 */

@Getter
public class GeneratorMultiplierItem {

    private final String id;
    private final double multiplier;
    private final ItemStack itemStack;

    public GeneratorMultiplierItem(String id, ConfigurationSection section) {
        this.id = id;
        this.multiplier = section.getDouble("multiplier");
        this.itemStack = new ItemBuilder(section.getString("item.material"))
                .setSkullOwner(section.getString("item.head"))
                .setDisplayName(section.getString("item.displayname"))
                .setLore(section.getStringList("item.lore"))
                .setEnchanted(section.getBoolean("item.enchanted"))
                .addPersistentData(PersistentDataUtil.MULTIPLIER_ITEM_ID, PersistentDataType.STRING, id)
                .build();
    }

    public void incrementMultiplier(User user, UserController userController) {
        user.addMultiplier(multiplier);
        userController.saveUser(user);
    }
}
