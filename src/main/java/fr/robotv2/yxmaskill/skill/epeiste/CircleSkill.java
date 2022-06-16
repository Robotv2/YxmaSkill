package fr.robotv2.yxmaskill.skill.epeiste;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.util.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class CircleSkill extends Skill {

    private final double RADIUS = 10D;
    private final double DAMAGE = 2.5D;

    public CircleSkill() {
        super("epeiste-circle", ClassType.EPEISTE);
    }

    @Override
    public boolean execute(GamePlayer invoker) {

        final Player invokerPlayer = invoker.getPlayer();
        final Location playerLoc = invoker.getPlayer().getLocation();
        final Collection<LivingEntity> entities = invoker.getPlayer().getWorld().getNearbyLivingEntities(playerLoc, RADIUS);

        for(LivingEntity entity : entities) {

            if(entity instanceof Player target && Objects.equals(target.getName(), invokerPlayer.getName())) {
                continue;
            }

            entity.damage(DAMAGE, invokerPlayer);
        }

        final double[] radians = new double[360];
        final double[] cos = new double[360];
        final double[] sin = new double[360];

        for(int i = 0; i < 5; i++) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(YxmaSkill.getInstance(), () -> {
                for(int degree = 0; degree < 360; degree++) {

                    double radian = radians[degree];
                    if(radian == 0) {
                        radian = Math.toRadians(degree);
                        radians[degree] = radian;
                    }

                    double x = cos[degree];
                    if(x == 0) {
                        x = Math.cos(radian);
                        cos[degree] = x;
                    }

                    double z = sin[degree];
                    if(z == 0) {
                        z = Math.sin(radian);
                        sin[degree] = z;
                    }

                    x = x * RADIUS;
                    z = z * RADIUS;

                    playerLoc.add(x,0,z);
                    ParticleUtil.sendToAllPlayers(Particle.VILLAGER_HAPPY, playerLoc);
                    playerLoc.subtract(x,0,z);
                }
            }, i * 2);
        }

        return true;
    }
}
