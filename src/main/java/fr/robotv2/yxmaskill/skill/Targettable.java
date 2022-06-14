package fr.robotv2.yxmaskill.skill;

import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.entity.LivingEntity;

public interface Targettable {
    boolean executeWithEntity(GamePlayer invoker, LivingEntity entity);
}
