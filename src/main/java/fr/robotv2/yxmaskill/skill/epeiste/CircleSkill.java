package fr.robotv2.yxmaskill.skill.epeiste;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.util.PaperUtil;
import fr.robotv2.yxmaskill.util.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class CircleSkill extends Skill {

    private final double RADIUS;
    private final double DAMAGE;

    public CircleSkill() {
        super("epeiste-circle", ClassType.EPEISTE);
        DAMAGE = getSection().getDouble("damage");
        RADIUS = getSection().getDouble("circle-radius");
    }

    private void drawCircle(Location origin, double radius) {

        final double[] radians = new double[360];
        final double[] cos = new double[360];
        final double[] sin = new double[360];

        final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.ORANGE, 1);

        for(int degree = 0; degree < 360; degree += 2) {

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

            x = x * radius;
            z = z * radius;

            origin.add(x,0,z);
            ParticleUtil.sendToAllPlayers(Particle.REDSTONE, origin, dustOptions);
            origin.subtract(x,0,z);
        }
    }

    @Override
    public boolean execute(GamePlayer invoker) {

        final Player invokerPlayer = invoker.getPlayer();
        final Location playerLoc = invoker.getPlayer().getLocation();
        final Collection<LivingEntity> entities = PaperUtil.getNearbyLivingEntities(playerLoc.getWorld(), playerLoc, RADIUS);

        for(LivingEntity entity : entities) {

            if(entity instanceof Player target && Objects.equals(target.getName(), invokerPlayer.getName())) {
                continue;
            }

            entity.damage(DAMAGE, invokerPlayer);
        }

        int count = 0;
        for(int line = (int) RADIUS - 1; line >= 0; line--) {
            final int finalLine = line;

            Bukkit.getScheduler().runTaskLater(YxmaSkill.getInstance(), () -> {
                this.drawCircle(playerLoc, RADIUS - finalLine);
            }, count * 2L);

            ++count;
        }

        return true;
    }
}
