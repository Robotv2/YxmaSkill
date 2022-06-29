package fr.robotv2.yxmaskill.ui.stock;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.ui.FillAPI;
import fr.robotv2.yxmaskill.ui.Gui;
import fr.robotv2.yxmaskill.ui.GuiManager;
import fr.robotv2.yxmaskill.ui.ItemAPI;
import fr.robotv2.yxmaskill.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class ChangeClassGui extends Gui {

    @Override
    public String getName(Player player, Object... objects) {
        return "&7Changer de classe &8>";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public void contents(Player player, Inventory inv, Object... objects) {
        FillAPI.setupEmptySlots(inv, FillAPI.FillType.ALL, FillAPI.getDefaultEmpty());

        ItemStack epeiste = ItemAPI.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMxNzU4YzdmMzFlODRiOWY0NDUxMWMyODlkZDJkMzNhYTY2ZTljMWE4NGZmMDg4MWQzMjhmYWYxMjQ0MDZlYyJ9fX0=");
        epeiste = ItemAPI.toBuilder(epeiste).setName("&8> " + ClassType.EPEISTE.getDisplay()).setLore(Collections.singletonList("&6Cliquez-ici pour changer de classe.")).build();
        inv.setItem(11, epeiste);

        ItemStack pugiliste = ItemAPI.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU5ZTA3N2RhMjVmY2M2N2FjYWQzNjRhYzUxZmMyYzlkN2IyMzhmYzU2NWE1MjM0YTM4ZGM5ZGM4MzEzYjMxOCJ9fX0=");
        pugiliste = ItemAPI.toBuilder(pugiliste).setName("&8> " + ClassType.PUGILISTE.getDisplay()).setLore(Collections.singletonList("&6Cliquez-ici pour changer de classe.")).build();
        inv.setItem(13, pugiliste);

        ItemStack sniper = ItemAPI.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjExYmZkNGUyNGZiN2Y1NmRlODg2YzZlMjkyYzM2ZWI3OGNkN2U1NDE3Y2E5YTA2Yzg1ZWE2YzlmOGFiNDcifX19");
        sniper = ItemAPI.toBuilder(sniper).setName("&8> " + ClassType.SNIPER.getDisplay()).setLore(Collections.singletonList("&6Cliquez-ici pour changer de classe.")).build();
        inv.setItem(15, sniper);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot, @NotNull ClickType click) {

        final ClassType target = switch (slot) {
            case 11 -> ClassType.EPEISTE;
            case 13 -> ClassType.PUGILISTE;
            case 15 -> ClassType.SNIPER;
            default -> null;
        };

        if(target == null) {
            return;
        }

        final GamePlayer gamePlayer = GamePlayer.getGamePlayer(player);

        if(gamePlayer.getClassType() == target) {
            player.closeInventory();
            ColorUtil.sendMessage(player, "&cVous n'avez pas changé de classe.");
        } else {
            player.closeInventory();
            gamePlayer.setClassType(target);
            ColorUtil.sendMessage(player, "&aVous venez de changer de classe !");
        }
    }

    @Override
    public void onClose(Player player, InventoryCloseEvent event) {
        Bukkit.getScheduler().runTaskLater(YxmaSkill.getInstance(), () -> {
            final GamePlayer gamePlayer = GamePlayer.getGamePlayer(player);
            if(gamePlayer.getClassType() == ClassType.NONE) {
                player.sendMessage(ChatColor.RED + "Vous devez absolument avoir une classe pour commencer le jeu !");
                GuiManager.open(player, ChangeClassGui.class);
            }
        }, 2);

    }
}
