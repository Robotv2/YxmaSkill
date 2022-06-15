package fr.robotv2.yxmaskill.skill.epeiste;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.skill.Targettable;
import fr.robotv2.yxmaskill.util.ColorUtil;
import fr.robotv2.yxmaskill.util.ParticleUtil;
import fr.robotv2.yxmaskill.util.Path;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashSkill extends Skill implements Targettable {

    private final double RADIUS = 5D;
    private final double DAMAGE = 2.5D;

    private enum TeleportLocation {
        RIGHT, LEFT;

        private float getNewYaw(float yaw) {
            switch (this) {
                case LEFT -> {
                    yaw -= 90;
                    return yaw;
                }
                case RIGHT -> {
                    yaw += 90;
                    return yaw;
                }
                default -> throw new IllegalStateException("Unexpected value: " + this);
            }
        }

        public Location getChangeLocation(Location location) {
            location = location.clone();
            location.setYaw(getNewYaw(location.getYaw()));
            Vector direction = location.getDirection().normalize();
            return location.add(direction);
        }
    }

    public DashSkill() {
        super("epeiste-dash", ClassType.EPEISTE);
    }

    @Override
    public boolean execute(GamePlayer invoker) {
        return true;
    }

    @Override
    public boolean executeWithEntity(GamePlayer invoker, LivingEntity entity) {

        final Player playerInvoker = invoker.getPlayer();

        final Location playerLocation = playerInvoker.getLocation();
        final Location entityLocation = entity.getLocation();

        if(playerLocation.distance(entityLocation) > RADIUS) {
            ColorUtil.sendMessage(playerInvoker, ChatColor.RED + "L'entité visée est trop loin.");
            return false;
        }

        final Vector inverseDirectionVec = entityLocation.getDirection().normalize().multiply(-1.5);
        final Location behindLocation = entityLocation.add(inverseDirectionVec);

        playerInvoker.teleport(behindLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        entity.damage(DAMAGE, playerInvoker);

        // Sound

        playerInvoker.playSound(behindLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        if(entity instanceof Player target) {
            target.playSound(behindLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }

        // Particle
        final Location START_RIGHT = TeleportLocation.RIGHT.getChangeLocation(playerLocation);
        final Location START_LEFT = TeleportLocation.LEFT.getChangeLocation(playerLocation);
        final Location END_RIGHT = TeleportLocation.RIGHT.getChangeLocation(behindLocation);
        final Location END_LEFT = TeleportLocation.LEFT.getChangeLocation(behindLocation);

        final List<Location> locations = Collections.synchronizedList(new ArrayList<>());
        locations.addAll(Path.getPath(START_RIGHT, END_RIGHT, 0.2));
        locations.addAll(Path.getPath(START_LEFT, END_LEFT, 0.2));

        for(int i = 0; i < 5; i++) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(YxmaSkill.getInstance(), () -> {
                ParticleUtil.sendToAllPlayers(Particle.PORTAL, locations);
            }, i * 2);
        }


        return true;
    }
}
