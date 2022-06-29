package fr.robotv2.yxmaskill.skill.epeiste;

import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.skill.Targettable;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class ConeSkill extends Skill implements Targettable {

    private final double RADIUS;

    public ConeSkill() {
        super("epeiste-cone", ClassType.EPEISTE);
        RADIUS = getSection().getDouble("range-radius");
    }

    private void showConeParticle(Location origin, Vector direction) {
        final Location destination = origin.clone().add(direction).multiply(2.5);
    }

    @Override
    public boolean execute(GamePlayer invoker) {
        return true;
    }

    @Override
    public boolean executeWithEntity(GamePlayer invoker, LivingEntity entity) {
        return false;
    }
}
