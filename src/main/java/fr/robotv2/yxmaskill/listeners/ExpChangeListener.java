package fr.robotv2.yxmaskill.listeners;

import fr.robotv2.yxmaskill.events.PlayerLevelUpEvent;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.util.rpgutil.LevelUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExpChangeListener implements Listener {

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        final GamePlayer gamePlayer = GamePlayer.getGamePlayer(event.getPlayer());

        final int level = gamePlayer.getLevel();
        final int amount = event.getAmount();
        event.setAmount(0);

        if(level >= LevelUtil.LEVEL_MAX) {
            return;
        }

        final double currentExp = gamePlayer.getExp() + amount;
        final double expToLevelUp = LevelUtil.requiredExp(level + 1);

        if(currentExp >= expToLevelUp) {
            gamePlayer.setLevel(level + 1);
            gamePlayer.setExp(0);
            Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(gamePlayer));
        } else {
            gamePlayer.setExp(currentExp);
        }

        gamePlayer.refreshExpBar();
    }
}
