package fr.robotv2.yxmaskill.listeners;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public record PlayerQuitListener(YxmaSkill plugin) implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final GamePlayer gamePlayer = GamePlayer.getGamePlayer(event.getPlayer());
        gamePlayer.save();
        GamePlayer.unregisterGamePlayer(gamePlayer);
    }
}
