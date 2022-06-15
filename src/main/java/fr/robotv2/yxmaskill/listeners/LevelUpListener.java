package fr.robotv2.yxmaskill.listeners;

import fr.robotv2.yxmaskill.events.PlayerLevelUpEvent;
import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelUpListener implements Listener {

    @EventHandler
    public void onLevelUp(PlayerLevelUpEvent event) {
        final GamePlayer gamePlayer = event.getGamePlayer();
        gamePlayer.refreshHearts();
    }
}
