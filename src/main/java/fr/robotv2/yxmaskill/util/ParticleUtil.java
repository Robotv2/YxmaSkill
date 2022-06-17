package fr.robotv2.yxmaskill.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ParticleUtil {

    public static void sendToAllPlayers(Particle particle, List<Location> locations) {
        Collections.unmodifiableCollection(Bukkit.getOnlinePlayers()).forEach(player -> locations.forEach(location -> player.spawnParticle(particle, location, 1)));
    }

    public static void sendToAllPlayers(Particle particle, Location location) {
        Collections.unmodifiableCollection(Bukkit.getOnlinePlayers()).forEach(player -> player.spawnParticle(particle, location, 1));
    }

    public static void comboLine(Player player, int combo) {

        if(combo < 0 || combo > 3) {
            return;
        }
    }
}
