package fr.robotv2.yxmaskill.listeners;

import fr.robotv2.yxmaskill.events.PlayerLevelUpEvent;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.util.rpgutil.LevelUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.EnumSet;

public class CaracListener implements Listener {

    final EnumSet<Characteristic> characteristics = EnumSet.allOf(Characteristic.class);

    public enum Characteristic {
        RESISTANCE,
        DEXTERITY,
        AGILITY,
        PRECISION,
        STRENGTH;
    }

    private void refreshAttribute(GamePlayer gamePlayer, Characteristic characteristic) {

        final Player player = gamePlayer.getPlayer();
        final int level = gamePlayer.getLevel();

        switch (characteristic) {

            case RESISTANCE -> {
                final AttributeInstance resistance = player.getAttribute(Attribute.GENERIC_ARMOR);
                if(resistance != null) {
                    final double multiplier = ((LevelUtil.CHARAC_RESISTANCE / 100) * level) + 1;
                    resistance.setBaseValue(resistance.getValue() * multiplier);
                }
            }

            case AGILITY -> {
                final AttributeInstance resistance = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
                if(resistance != null) {
                    final double multiplier = ((LevelUtil.CHARAC_AGILITY / 100) * level) + 1;
                    resistance.setBaseValue(resistance.getValue() * multiplier);
                }
            }

            case STRENGTH -> {
                final AttributeInstance resistance = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                if(resistance != null) {
                    final double multiplier = ((LevelUtil.CHARAC_STRENGTH / 100) * level) + 1;
                    resistance.setBaseValue(resistance.getValue() * multiplier);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        final GamePlayer gamePlayer = GamePlayer.getGamePlayer(event.getPlayer());
        characteristics.forEach(charac -> refreshAttribute(gamePlayer, charac));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLevelUp(PlayerLevelUpEvent event) {
        characteristics.forEach(charac -> refreshAttribute(event.getGamePlayer(), charac));
    }
}
