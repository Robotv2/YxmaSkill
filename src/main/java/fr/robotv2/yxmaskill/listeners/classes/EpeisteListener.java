package fr.robotv2.yxmaskill.listeners.classes;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.events.PlayerLevelUpEvent;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.util.ParticleUtil;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class EpeisteListener extends ClassesListener {

    private final EnumSet<Material> WEAPONS = EnumSet.of(
            Material.WOODEN_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD);

    private final Cache<String, Integer> combo = CacheBuilder
            .newBuilder()
            .expireAfterWrite(500, TimeUnit.MILLISECONDS)
            .build();

    public EpeisteListener() {
        super(ClassType.EPEISTE);
    }

    private void refreshStrength(GamePlayer gamePlayer) {
        final ConfigurationSection section = gamePlayer.getClassType().getSection();

        if(section == null || !gamePlayer.isValid()) {
            return;
        }

        final double pourcentage = section.getDouble("strength." + gamePlayer.getLevel(), -1);

        if(pourcentage == -1) {
            return;
        }

        final AttributeInstance instance = gamePlayer.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if(instance == null) return;

        instance.setBaseValue(instance.getDefaultValue() * (1 + (pourcentage / 100)));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItem();

        if(!this.hasClass(player) || itemStack == null || !WEAPONS.contains(itemStack.getType())) {
            return;
        }

        event.setCancelled(true);

        Integer currentCombo = combo.getIfPresent(player.getName());
        if(currentCombo == null || currentCombo > 3) {
            currentCombo = 1;
        }

        ParticleUtil.comboLine(player, currentCombo);
        ++currentCombo;
        combo.put(player.getName(), currentCombo);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if(!this.hasClass(player)) {
            return;
        }

        final GamePlayer gamePlayer = GamePlayer.getGamePlayer(player);
        this.refreshStrength(gamePlayer);
    }

    @EventHandler
    public void onLevelUp(PlayerLevelUpEvent event) {
        final GamePlayer player = event.getGamePlayer();
        if(!this.hasClass(player)) return;
        this.refreshStrength(player);
    }
}
