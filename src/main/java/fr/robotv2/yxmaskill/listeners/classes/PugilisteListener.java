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

public class PugilisteListener extends ClassesListener {

    public PugilisteListener() {
        super(ClassType.PUGILISTE);
    }

    private void refreshResistance(GamePlayer gamePlayer) {
        final ConfigurationSection section = gamePlayer.getClassType().getSection();

        if(section == null || !gamePlayer.isValid()) {
            return;
        }

        final double value = section.getDouble("resistance." + gamePlayer.getLevel(), -1);

        if(value == -1) {
            return;
        }

        final AttributeInstance instance = gamePlayer.getPlayer().getAttribute(Attribute.GENERIC_ARMOR);
        if(instance == null) return;

        instance.setBaseValue(value / 10);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if(!this.hasClass(player)) {
            return;
        }

        this.refreshResistance(GamePlayer.getGamePlayer(player));
    }

    @EventHandler
    public void onLevelUp(PlayerLevelUpEvent event) {
        final GamePlayer player = event.getGamePlayer();
        if(!this.hasClass(player)) return;
        this.refreshResistance(player);
    }
}
