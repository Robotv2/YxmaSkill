package fr.robotv2.yxmaskill.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class PaperUtil {

    public static Collection<LivingEntity> getNearbyLivingEntities(World world, Location location, double radius) {

        if(world == null) {
            return Collections.emptyList();
        }

        return world.getNearbyEntities(location, radius, radius, radius).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(LivingEntity.class::cast)
                .toList();
    }

    @Nullable
    public static Entity getTargetEntity(Player player, int radius) {

        final Location startLocation = player.getEyeLocation().clone();
        startLocation.add(player.getEyeLocation().getDirection().normalize());

        final RayTraceResult result = player.getWorld().rayTraceEntities(startLocation, player.getEyeLocation().getDirection(), radius);
        return result != null ? result.getHitEntity() : null;
    }
}
