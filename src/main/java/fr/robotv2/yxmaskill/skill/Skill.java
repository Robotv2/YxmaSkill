package fr.robotv2.yxmaskill.skill;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class Skill implements Listener {

    private final String id;
    private final int cooldown;
    private final ClassType classType;

    public Skill(String id, ClassType type, int cooldown) {
        this.id = id;
        this.cooldown = cooldown;
        this.classType = type;
    }

    public Skill(String id, ClassType type) {
        this(id, type, 30);
    }

    /**
     * trigger whenever an entity cast the skill.
     */
    public abstract boolean execute(GamePlayer invoker);

    public Skill registerListener() {
        Bukkit.getPluginManager().registerEvents(this, YxmaSkill.getInstance());
        return this;
    }

    public String getId() {
        return id;
    }

    public int getCooldown() {
        return cooldown;
    }

    public ClassType getClassType() {
        return classType;
    }
}
