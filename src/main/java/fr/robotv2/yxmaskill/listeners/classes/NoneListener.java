package fr.robotv2.yxmaskill.listeners.classes;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.ui.GuiManager;
import fr.robotv2.yxmaskill.ui.stock.ChangeClassGui;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class NoneListener extends ClassesListener {

    public NoneListener() {
        super(ClassType.NONE);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if(!this.hasClass(event.getPlayer())) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(YxmaSkill.getInstance(), () -> {
            GuiManager.open(event.getPlayer(), ChangeClassGui.class);
        }, 20);
    }
}
