package fr.robotv2.yxmaskill.listeners.classes;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.effect.SphereEffect;
import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
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


    }
}
