package fr.robotv2.yxmaskill.ui;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class FillAPI {

    private static ItemStack empty;

    public enum FillType {
        ALL, BOTTOM, TOP, BOTTOM_AND_TOP;
    }

    public static ItemStack getDefaultEmpty() {
        final ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        final ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(null);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void setEmpty(ItemStack item) {
        empty = item;
    }

    public static ItemStack getEmpty() {
        if(empty == null)
            empty = getDefaultEmpty();
        return empty;
    }

    public static void setupEmptySlots(Inventory inv, FillType type, @Nullable ItemStack item) {
        final ItemStack fill = item != null ? item : getEmpty();
        switch(type) {
            case ALL:
                for(int i=0; i <= inv.getSize() - 1; i++) {
                    inv.setItem(i, fill);
                }
                break;

            case TOP:
                for(int i=0; i <= 8; i++) {
                    inv.setItem(i, fill);
                }
                break;

            case BOTTOM:
                for(int i = inv.getSize() - 8; i <= inv.getSize(); i++) {
                    inv.setItem(i, fill);
                }
                break;

            case BOTTOM_AND_TOP:
                setupEmptySlots(inv, FillType.TOP, fill);
                setupEmptySlots(inv, FillType.BOTTOM, fill);
                break;
        }
    }
}
