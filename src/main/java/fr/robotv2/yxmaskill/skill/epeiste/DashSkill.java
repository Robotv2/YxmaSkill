package fr.robotv2.yxmaskill.skill.epeiste;

import de.slikey.effectlib.effect.LineEffect;
import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.skill.Targettable;
import fr.robotv2.yxmaskill.util.ColorUtil;
import fr.robotv2.yxmaskill.util.LocationUtil;
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

    private double RADIUS;
    private double DAMAGE;

    private enum TeleportLocation {
        RIGHT, LEFT;

        private float getNewYaw(float yaw) {
            switch (this) {
                case LEFT -> {
                    yaw += 90;
                    return yaw;
                }
                case RIGHT -> {
                    yaw -= 90;
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
        DAMAGE = getSection().getDouble("damage");
        RADIUS = getSection().getDouble("max-radius-entity");
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

        final double distance = playerLocation.distance(entityLocation);

        if(playerLocation.distance(entityLocation) > RADIUS) {
            ColorUtil.sendMessage(playerInvoker, ChatColor.RED + "L'entité visée est trop loin.");
            return false;
        }

        final Vector inverseDirectionVec = playerLocation.getDirection().normalize().multiply(distance + 5);
        Location behindLocation = playerLocation.clone().add(inverseDirectionVec);
        boolean safe = false;

        for(int i = 0; i < 4; i++) {

            if(LocationUtil.isSafe(behindLocation)) {
                safe = true;
                break;
            }

            behindLocation.add(0, 1, 0);
        }

        if(!safe) {
            behindLocation = entityLocation;
        }

        playerInvoker.teleportAsync(behindLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        entity.damage(DAMAGE, playerInvoker);

        // Sound

        playerInvoker.playSound(behindLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        if(entity instanceof Player target) {
            target.playSound(behindLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }

        // Particle
        final Location START_RIGHT = TeleportLocation.RIGHT.getChangeLocation(playerLocation.add(0, 1, 0));
        final Location START_LEFT = TeleportLocation.LEFT.getChangeLocation(playerLocation);
        final Location END_RIGHT = TeleportLocation.RIGHT.getChangeLocation(behindLocation.add(0, 1, 0));
        final Location END_LEFT = TeleportLocation.LEFT.getChangeLocation(behindLocation);

        final List<Location> locations = Collections.synchronizedList(new ArrayList<>());
        locations.addAll(Path.getPath(START_RIGHT, END_RIGHT, 0.2));
        locations.addAll(Path.getPath(START_LEFT, END_LEFT, 0.2));

        for(int i = 0; i < 5; i++) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(YxmaSkill.getInstance(), () -> {
                ParticleUtil.sendToAllPlayers(Particle.VILLAGER_HAPPY, locations);
            }, i * 2);
        }

        return true;
    }
}
