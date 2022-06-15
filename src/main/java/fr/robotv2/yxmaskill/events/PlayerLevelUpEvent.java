package fr.robotv2.yxmaskill.events;

import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLevelUpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final GamePlayer gamePlayer;
    private final int from;
    private final int to;

    public PlayerLevelUpEvent(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        this.from = gamePlayer.getLevel() - 1;
        this.to = gamePlayer.getLevel();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
