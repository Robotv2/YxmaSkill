package fr.robotv2.yxmaskill.classes;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.skill.SkillManager;
import fr.robotv2.yxmaskill.util.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedList;

public enum ClassType {

    EPEISTE,
    PUGILISTE,
    SNIPER,
    NONE;

    private final ConfigurationSection section;
    ClassType() {
        final FileConfiguration configuration = YxmaSkill.getInstance().getClassConfiguration().get();
        this.section = configuration.getConfigurationSection("class." + toLowerCase());
    }

    public String toLowerCase() {
        return toString().toLowerCase();
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public String getDisplay() {
        return ColorUtil.colorize(getSection().getString("display"));
    }

    public LinkedList<Skill> getSkills() {

        final LinkedList<Skill> result = new LinkedList<>();
        if(section == null) {
            return result;
        }

        final SkillManager skillManager = YxmaSkill.getInstance().getSkillManager();

        getSection().getStringList("skills").stream()
                .filter(skillManager::exist)
                .map(skillManager::getSkill)
                .forEachOrdered(result::add);

        return result;
    }
}
