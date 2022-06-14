package fr.robotv2.yxmaskill;

import fr.robotv2.yxmaskill.skill.SkillManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class YxmaSkill extends JavaPlugin {

    private static YxmaSkill instance;
    private final SkillManager skillManager = new SkillManager();

    public static YxmaSkill getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }
}
