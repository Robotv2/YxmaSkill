package fr.robotv2.yxmaskill.classes;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.skill.SkillManager;
import revxrsal.commands.util.Collections;

import java.util.LinkedList;

public enum ClassType {

    EPEISTE(Collections.linkedListOf("epeiste-dash", "", "epeiste-circle")),
    PUGILISTE(Collections.linkedListOf()),
    SNIPER(Collections.linkedListOf());

    private final LinkedList<String> linkedSkills;
    ClassType(LinkedList<String> linkedSkills) {
        this.linkedSkills = linkedSkills;
    }

    public String toLowerCase() {
        return toString().toLowerCase();
    }

    public LinkedList<Skill> getSkills() {
        final SkillManager skillManager = YxmaSkill.getInstance().getSkillManager();
        final LinkedList<Skill> result = new LinkedList<>();
        linkedSkills.stream()
                .filter(skillManager::exist)
                .map(skillManager::getSkill)
                .forEachOrdered(result::add);
        return result;
    }
}
