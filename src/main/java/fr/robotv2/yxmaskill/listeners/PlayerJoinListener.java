package fr.robotv2.yxmaskill.listeners;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public record PlayerJoinListener(YxmaSkill plugin) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        final UUID playerUUID = event.getPlayer().getUniqueId();
        GamePlayer gamePlayer = plugin.getDataManager().getGamePlayer(playerUUID);

        if(gamePlayer == null) {
            gamePlayer = new GamePlayer(playerUUID, ClassType.EPEISTE);
        }

        GamePlayer.registerGamePlayer(gamePlayer);
        gamePlayer.refreshHearts();
        gamePlayer.refreshExpBar();

        plugin.getLogger().info(String.format("Data for the player %s has been loaded successfully.", event.getPlayer().getName()));
    }
}
