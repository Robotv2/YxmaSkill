package fr.robotv2.yxmaskill.listeners.classes;

import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.events.PlayerLevelUpEvent;
import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class EpeisteListener extends ClassesListener {

    public EpeisteListener() {
        super(ClassType.EPEISTE);
    }

    private void refreshStrength(GamePlayer gamePlayer) {
        final ConfigurationSection section = gamePlayer.getClassType().getSection();

        if(section == null || !gamePlayer.isValid()) {
            return;
        }

        final double pourcentage = section.getDouble("strength." + gamePlayer.getLevel(), -1);

        if(pourcentage == -1) {
            return;
        }

        final AttributeInstance instance = gamePlayer.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if(instance == null) return;

        instance.setBaseValue(instance.getDefaultValue() * (1 + (pourcentage / 100)));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if(!this.hasClass(player)) {
            return;
        }

        this.refreshStrength(GamePlayer.getGamePlayer(player));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLevelUp(PlayerLevelUpEvent event) {
        final GamePlayer player = event.getGamePlayer();
        if(!this.hasClass(player)) return;
        this.refreshStrength(player);
    }
}
