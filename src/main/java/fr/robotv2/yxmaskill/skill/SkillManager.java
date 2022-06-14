package fr.robotv2.yxmaskill.skill;

import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SkillManager {

    private final Map<String, Skill> skills = new ConcurrentHashMap<>();

    public void clearSkills() {
        this.skills.clear();
    }

    public void registerSkill(Skill skill) {
        this.skills.put(skill.getId().toLowerCase(), skill);
    }

    public void unregisterSkill(Skill skill) {
        this.skills.remove(skill.getId());
    }

    @Nullable
    public Skill getSkill(String id) {
        return skills.get(id.toLowerCase());
    }

    public void cast(GamePlayer invoker, Skill skill, boolean bypassCooldown) {

        if(!bypassCooldown && !invoker.canCast(skill)) {
            return;
        }

        boolean result = false;

        if(skill instanceof Targettable targettableSkill) {
            final Entity target = invoker.getPlayer().getTargetEntity(20, false);
            if (target instanceof LivingEntity livingEntity) {
                result = targettableSkill.executeWithEntity(invoker, livingEntity);
            }
        } else {
            result = skill.execute(invoker);
        }

        if(!result) {
            return;
        }

        invoker.resetCooldown(skill);
    }
}
