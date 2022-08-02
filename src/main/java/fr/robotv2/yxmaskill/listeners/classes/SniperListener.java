package fr.robotv2.yxmaskill.listeners.classes;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.events.PlayerLevelUpEvent;
import fr.robotv2.yxmaskill.player.GamePlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SniperListener extends ClassesListener{

    public SniperListener() {
        super(ClassType.SNIPER);
    }

    private void refreshSpeed(GamePlayer gamePlayer) {
        final ConfigurationSection section = gamePlayer.getClassType().getSection();

        if(section == null || !gamePlayer.isValid()) {
            return;
        }

        final double pourcentage = section.getDouble("speed." + gamePlayer.getLevel(), -1);

        if(pourcentage == -1) {
            return;
        }

        final AttributeInstance instance = gamePlayer.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if(instance == null) return;

        instance.setBaseValue(instance.getDefaultValue() * (1 + (pourcentage / 100)));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if(!this.hasClass(player)) {
            return;
        }

        this.refreshSpeed(GamePlayer.getGamePlayer(player));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLevelUp(PlayerLevelUpEvent event) {
        final GamePlayer player = event.getGamePlayer();

        if(!this.hasClass(player)) {
            return;
        }

        this.refreshSpeed(player);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {

        if(!(event.getEntity() instanceof Player player)) {
            return;
        }

        if(!(event.getProjectile() instanceof Arrow arrow)) {
            return;
        }

        if(!this.hasClass(player)) {
            return;
        }

        new BukkitRunnable() {
            public void run() {
                if (arrow.isOnGround() || arrow.isDead()) {
                    this.cancel();
                    return;
                }
                arrow.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 0);
            }
        }.runTaskTimer(YxmaSkill.getInstance(), 0, 1);

        Bukkit.getScheduler().runTaskLater(YxmaSkill.getInstance(), () -> {
            for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(arrow.getEntityId());
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }, 1);
    }
}
