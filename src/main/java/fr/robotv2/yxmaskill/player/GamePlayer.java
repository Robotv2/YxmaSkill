package fr.robotv2.yxmaskill.player;

import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.skill.Skill;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class GamePlayer {

    private final UUID playerUUID;
    private ClassType type;

    private int level;
    private double exp;

    private final Map<String, Long> caches = new ConcurrentHashMap<>();

    public GamePlayer(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public UUID getUniqueId() {
        return playerUUID;
    }

    public ClassType getClassType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void sendParticle(Particle particle, Location location) {
        getPlayer().spawnParticle(particle, location, 1);
    }

    public void sendParticle(Supplier<Particle> supplier, Location location) {
        sendParticle(supplier.get(), location);
    }

    public long getCooldown(Skill skill) {
        return caches.getOrDefault(skill.getId(), -1L);
    }

    public void resetCooldown(Skill skill) {
        this.caches.put(skill.getId(), System.currentTimeMillis());
    }

    public boolean canCast(Skill skill) {
        final long current = getCooldown(skill);
        if(current == -1) {
            return true;
        } else {
            return (System.currentTimeMillis() - current) >= skill.getCooldown() * 1000L;
        }
    }
}
