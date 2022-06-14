package fr.robotv2.yxmaskill.skill.epeiste;

import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.skill.Targettable;
import fr.robotv2.yxmaskill.util.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class DashSkill extends Skill implements Targettable {

    private final double RADIUS = 5D;
    private final double DAMAGE = 2.5D;

    public DashSkill() {
        super("epeiste-dash", ClassType.EPEISTE);
    }

    @Override
    public boolean execute(GamePlayer invoker) {
        return true;
    }

    @Override
    public boolean executeWithEntity(GamePlayer invoker, LivingEntity entity) {

        final Player playerInvoker = invoker.getPlayer();

        final Location playerLocation = playerInvoker.getLocation();
        final Location entityLocation = entity.getLocation();

        if(playerLocation.distance(entityLocation) > RADIUS) {
            ColorUtil.sendMessage(playerInvoker, ChatColor.RED + "L'entité visée est trop loin.");
            return false;
        }

        final Vector inverseDirectionVec = entityLocation.getDirection().normalize().multiply(-1.5);
        final Location behindLocation = entityLocation.add(inverseDirectionVec);

        playerInvoker.teleportAsync(behindLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        entity.damage(DAMAGE, playerInvoker);

        return true;
    }
}
