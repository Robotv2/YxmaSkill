package fr.robotv2.yxmaskill.listeners;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public record SkillBookListener(YxmaSkill plugin) implements Listener {

    private boolean isSkillBook(ItemStack item) {

        if (item == null) {
            return false;
        }

        final ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(Skill.SKILL_KEY, PersistentDataType.STRING);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) {
            return;
        }

        final ItemStack item = event.getItem();

        if (!isSkillBook(item)) {
            return;
        }

        event.setCancelled(true);
        final ItemMeta meta = item.getItemMeta();
        final String skillId = meta.getPersistentDataContainer().get(Skill.SKILL_KEY, PersistentDataType.STRING);

        if (skillId == null) {
            return;
        }

        final GamePlayer gamePlayer = GamePlayer.getGamePlayer(player);
        final Skill skill = plugin.getSkillManager().getSkill(skillId);

        if(skill == null) {
            return;
        }

        if(gamePlayer.getSkillLevel(skill) == 0) {
            ColorUtil.sendMessage(gamePlayer, "&cVous n'avez pas encore débloqué ce skill.");
            return;
        }

        plugin.getSkillManager().cast(gamePlayer, skill, false);
    }

    @EventHandler
    public void onDrop(EntityDropItemEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (isSkillBook(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        final Inventory inventory = event.getClickedInventory();

        if (inventory == null || inventory.getType() != InventoryType.PLAYER) {
            return;
        }

        if (isSkillBook(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(this::isSkillBook);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        GamePlayer.getGamePlayer(event.getPlayer()).refreshSkillsItem();
    }
}
