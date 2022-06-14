package fr.robotv2.yxmaskill.skill;

import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;

public abstract class Skill {

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
