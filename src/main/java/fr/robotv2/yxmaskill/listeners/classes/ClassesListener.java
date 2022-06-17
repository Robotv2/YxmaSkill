package fr.robotv2.yxmaskill.listeners.classes;

import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class ClassesListener implements Listener {

    private final ClassType type;
    public ClassesListener(ClassType type) {
        this.type = type;
    }

    public boolean hasClass(Player player) {
        return hasClass(GamePlayer.getGamePlayer(player));
    }

    public boolean hasClass(GamePlayer gamePlayer) {
        return gamePlayer.getClassType() == type;
    }
}
