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
public class GeneratorSlotItem {

    private final String id;
    private final ItemStack itemStack;

    public GeneratorSlotItem(String id, ConfigurationSection section) {
        this.id = id;
        this.itemStack = new ItemBuilder(section.getString("material"))
                .setSkullOwner(section.getString("head"))
                .setDisplayName(section.getString("displayname"))
                .setLore(section.getStringList("lore"))
                .setEnchanted(section.getBoolean("enchanted"))
                .addPersistentData(PersistentDataUtil.SLOT_ITEM_ID, PersistentDataType.STRING, id)
                .build();
    }

    public void incrementSlot(User user, UserController userController) {
        user.addMaxGenerator(Integer.parseInt(id));
        userController.saveUser(user);
    }
}
