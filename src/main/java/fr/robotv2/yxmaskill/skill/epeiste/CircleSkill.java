package fr.robotv2.yxmaskill.skill.epeiste;

import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class CircleSkill extends Skill {

    private final double RADIUS = 5D;
    private final double DAMAGE = 2.5D;

    public CircleSkill(String id, ClassType type) {
        super("epeiste-circle", ClassType.EPEISTE);
    }

    @Override
    public boolean execute(GamePlayer invoker) {

        final Location playerLoc = invoker.getPlayer().getLocation();
        final Collection<LivingEntity> entities = invoker.getPlayer().getWorld().getNearbyLivingEntities(playerLoc, RADIUS);

        for(LivingEntity entity : entities) {
            entity.damage(DAMAGE, invoker.getPlayer());
        }

        for(int i = 0; i < 5; i++) {
            for(int degree = 0; degree < 360; degree++) {
                double radians = Math.toRadians(degree);
                double x = Math.cos(radians);
                double z = Math.sin(radians);

                playerLoc.add(x,0,z);
                invoker.sendParticle(Particle.FLAME, playerLoc);
                playerLoc.subtract(x,0,z);
            }
        }

        return true;
    }
}
