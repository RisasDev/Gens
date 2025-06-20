package dev.risas.zurixgens.utilities.menu.buttons;

import dev.risas.zurixgens.utilities.ItemBuilder;
import dev.risas.zurixgens.utilities.menu.Button;
import dev.risas.zurixgens.utilities.menu.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageButton extends Button {

    private final int mod;
    private final PaginatedMenu menu;

    @Override
    public ItemStack getItemStack(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.LEVER);

        if (hasNext()) {
            itemBuilder.setDisplayName(mod > 0 ? "&aNext page" : "&cPrevious page");
        }
        else {
            itemBuilder.setDisplayName("&7" + (mod > 0 ? "Last page" : "First page"));
        }

        return itemBuilder.build();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        if (hasNext()) {
            menu.modPage(mod);
            playNeutral(player);
        }
        else {
            playFailure(player);
        }
    }

    private boolean hasNext() {
        int pg = menu.getPage() + mod;
        return pg > 0 && menu.getPages() >= pg;
    }

    @Override
    public boolean isCloseableAfterClick() {
        return false;
    }
}
