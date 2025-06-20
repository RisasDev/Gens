package dev.risas.zurixgens.utilities.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
public abstract class Menu {

    protected static Map<UUID, Menu> menus = new HashMap<>();
    protected Map<Integer, Button> buttons = new HashMap<>();

    protected Player player;
    protected Inventory inventory;
    protected String title;
    protected boolean allowInteract, updateAfterClick;

    public Menu(Player player, String title, int rows) {
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 9 * rows, title);
        this.allowInteract = false;
        this.title = title;
    }

    public void open() {
        this.buttons = this.getButtons();

        inventory.clear();

        for (Map.Entry<Integer, Button> entry : buttons.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().getItemStack(player));
        }

        player.openInventory(inventory);

        menus.put(player.getUniqueId(), this);
    }

    public void close(Player player) {
        menus.remove(player.getUniqueId());
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;

        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public abstract Map<Integer, Button> getButtons();

    public int getSize() {
        return inventory.getSize();
    }

    public static Menu getMenu(Player player) {
        return menus.get(player.getUniqueId());
    }

    public static boolean hasMenu(Player player) {
        return menus.containsKey(player.getUniqueId());
    }
}