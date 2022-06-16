package fr.robotv2.yxmaskill.player;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.util.rpgutil.LevelUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@DatabaseTable(tableName = "player_data")
public class GamePlayer {

    @DatabaseField(columnName = "player_uuid", id = true, unique = true)
    private UUID playerUUID;

    @DatabaseField(columnName = "class")
    private ClassType type;

    @DatabaseField(columnName = "level")
    private int level = 1;

    @DatabaseField(columnName = "exp")
    private double exp = 0;

    @DatabaseField(columnName = "skills-levels", dataType = DataType.SERIALIZABLE)
    private final ConcurrentHashMap<String, Integer> skillLevels = new ConcurrentHashMap<>();

    @DatabaseField(columnName = "point-competences")
    private int compPoint = 0;

    private final Map<String, Long> caches = new ConcurrentHashMap<>();

    public GamePlayer() {}

    public GamePlayer(UUID playerUUID, ClassType type) {
        this.playerUUID = playerUUID;
        this.type = type;
    }

    // <<- INFO ->>

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public UUID getUniqueId() {
        return playerUUID;
    }

    public ClassType getClassType() {
        return type;
    }

    public void save() {
        YxmaSkill.getInstance().getDataManager().saveGamePlayer(this);
    }

    // <<- LEVEL & EXP ->>

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

    // <<- SKILL ->>

    public int getSkillLevel(Skill skill) {
        return this.skillLevels.getOrDefault(skill.getId(), 1);
    }

    public void setSkillLevel(Skill skill, int value) {
        this.skillLevels.put(skill.getId(), value);
    }

    // <<- PARTICLE ->>

    public void sendParticle(Particle particle, Location location) {
        getPlayer().spawnParticle(particle, location, 1);
    }

    public void sendParticle(Supplier<Particle> supplier, Location location) {
        sendParticle(supplier.get(), location);
    }

    // <<- REFRESH ->>

    public void heal() {
        if(!isValid()) return;
        final Player player = getPlayer();
        final AttributeInstance attributeInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(attributeInstance == null) return;
        player.setHealth(attributeInstance.getBaseValue());
    }

    public void refreshHearts() {
        if(!isValid()) return;
        final Player player = getPlayer();
        final AttributeInstance attributeInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(attributeInstance == null) return;
        attributeInstance.setBaseValue(attributeInstance.getDefaultValue() + (getLevel() - 1));
    }

    public void refreshExpBar() {
        if(!isValid()) return;

        final Player player = getPlayer();
        final int level = getLevel();

        if(level >= LevelUtil.LEVEL_MAX) {
            player.setLevel(level);
            player.setExp(0.9999F);
            return;
        }

        final double expToLevelUp = LevelUtil.requiredExp(level + 1);

        player.setLevel(level);

        if(getExp() == 0) {
            player.setExp(0);
        } else {
            player.setExp((float) (getExp() / (expToLevelUp)));
        }
    }

    public void refreshSkillsItem() {
        if(!isValid()) return;

        final Player player = getPlayer();
        final LinkedList<Skill> skills = getClassType().getSkills();

        for(int i = 6; i <= 8; i++) {
            final Skill skill = skills.pollFirst();

            if(skill == null) {
                continue;
            }

            player.getInventory().setItem(i, skill.getItem(this));
        }
    }

    // <<- COOLDOWN ->>

    public long getCooldown(Skill skill) {
        return caches.getOrDefault(skill.getId(), -1L);
    }

    public void resetCooldown(Skill skill) {
        this.caches.put(skill.getId(), System.currentTimeMillis());
    }

    public double getRemainingCooldown(Skill skill) {
        final long current = getCooldown(skill);
        if(current == -1) {
            return 0L;
        } else {
            return (skill.getCooldown() * 1000) - (System.currentTimeMillis() - current) / 1000D;
        }
    }

    public boolean canCast(Skill skill) {
        final long current = getCooldown(skill);
        if(current == -1) {
            return true;
        } else {
            return System.currentTimeMillis() - current >= skill.getCooldown() * 1000L;
        }
    }

    // <<- COMP POINT ->>

    public int getCompPoint() {
        return compPoint;
    }

    public void setCompPoint(int value) {
        this.compPoint = value;
    }

    public void addCompPoint(int value) {
        this.compPoint += value;
    }

    public void removeCompPoint(int value) {
        if(this.compPoint - value < 0) {
            this.compPoint = 0;
        } else {
            this.compPoint -= value;
        }
    }

    // <<- UTIL ->>

    public boolean isValid() {
        final Player player = getPlayer();
        return player != null && player.isOnline();
    }

    // <<- STATIC METHOD ->>

    static Map<UUID, GamePlayer> gamePlayers = new ConcurrentHashMap<>();

    public static void registerGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.put(gamePlayer.playerUUID, gamePlayer);
    }

    public static void unregisterGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.remove(gamePlayer.playerUUID);
    }

    public static GamePlayer getGamePlayer(UUID playerUUID) {
        return gamePlayers.get(playerUUID);
    }

    public static GamePlayer getGamePlayer(Player player) {
        return getGamePlayer(player.getUniqueId());
    }

    public static Collection<GamePlayer> getGamesPlayers() {
        return Collections.unmodifiableCollection(gamePlayers.values());
    }
}
