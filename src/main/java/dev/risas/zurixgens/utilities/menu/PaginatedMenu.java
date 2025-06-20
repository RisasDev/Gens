package dev.risas.zurixgens.utilities.menu;

import dev.risas.zurixgens.utilities.FileConfig;
import dev.risas.zurixgens.utilities.menu.buttons.PageButton;
import dev.risas.zurixgens.utilities.menu.decoration.Decoration;
import dev.risas.zurixgens.utilities.menu.decoration.DecorationUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Risas
 * @date 20-06-2025
 * @discord https://risas.me/discord
 */

@Getter
public abstract class PaginatedMenu extends Menu {

    private int page = 1;
    private final int maxItems;
    private Set<Decoration> decorations;

    public PaginatedMenu(
            Player player,
            String title,
            int rows,
            int maxItems) {
        super(player, title, rows);
        this.maxItems = maxItems;
    }

    public PaginatedMenu(
            Player player,
            String title,
            int rows,
            int maxItems,
            FileConfig menusFile,
            String path) {
        this(player, title, rows, maxItems);
        this.decorations = new HashSet<>();

        ConfigurationSection section = menusFile.getConfiguration().getConfigurationSection(path);
        DecorationUtil.registerDecorations(section, decorations);
    }

    public void modPage(int mod) {
        page += mod;
        buttons.clear();
        open();
    }

    public final int getPages() {
        int buttonAmount = getAllPagesButtons().size();
        if (buttonAmount == 0) return 1;

        return (int) Math.ceil(buttonAmount / (double) maxItems);
    }

    @Override
    public final Map<Integer, Button> getButtons() {
        HashMap<Integer, Button> buttons = new HashMap<>(getGlobalButtons());

        int minIndex = (int) ((double) (page - 1) * maxItems);
        int maxIndex = (int) ((double) (page) * maxItems);

        int index = 0;

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons().entrySet()) {
            if (index >= minIndex && index < maxIndex) {
                int slot = 0;

                while (slot < getSize() && buttons.containsKey(slot)) {
                    slot++;
                }

                if (slot < getSize()) {
                    buttons.put(slot, entry.getValue());
                }
            }

            index++;
        }

        return buttons;
    }

    public Map<Integer, Button> getGlobalButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        if (decorations != null) {
            for (Decoration decoration : decorations) {
                buttons.put(decoration.getSlot(), Button.getButton(decoration.getItemStack()));
            }
        }

        buttons.put(getSize() - 9, new PageButton(-1, this));
        buttons.put(getSize() - 1, new PageButton(1, this));

        return buttons;
    }

    public abstract Map<Integer, Button> getAllPagesButtons();
}
