package dev.risas.zurixgens.utilities;

import dev.risas.zurixgens.ZurixGens;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

/**
 * @author Risas
 * @date 15-06-2025
 * @discord https://risas.me/discord
 */

@UtilityClass
public class PersistentDataUtil {

    public NamespacedKey GENERATOR_ID = new NamespacedKey(ZurixGens.getInstance(), "generator_id");
    public NamespacedKey GENERATOR_DROP_ID = new NamespacedKey(ZurixGens.getInstance(), "generator_drop_id");
    public NamespacedKey GENERATOR_DROP_PRICE = new NamespacedKey(ZurixGens.getInstance(), "generator_drop_price");
    public NamespacedKey GENERATOR_DROP_GLOW = new NamespacedKey(ZurixGens.getInstance(), "generator_drop_glow");
    public NamespacedKey GENERATOR_DROP_ENCHANTED = new NamespacedKey(ZurixGens.getInstance(), "generator_drop_enchanted");
    public NamespacedKey SLOT_ITEM_ID = new NamespacedKey(ZurixGens.getInstance(), "slot_item_id");
    public NamespacedKey MULTIPLIER_ITEM_ID = new NamespacedKey(ZurixGens.getInstance(), "multiplier_item_id");
}
