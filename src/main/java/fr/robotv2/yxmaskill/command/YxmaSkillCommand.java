package fr.robotv2.yxmaskill.command;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.events.PlayerLevelUpEvent;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.ui.GuiManager;
import fr.robotv2.yxmaskill.ui.stock.ChangeClassGui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("yxmaskill")
public record YxmaSkillCommand(YxmaSkill plugin) {

    @Subcommand("reload")
    @Usage("reload")
    @CommandPermission("yxmaskill.command.reload")
    @Description("Reload the plugin.")
    public void onReload(BukkitCommandActor actor) {
        plugin.onReload();
        actor.reply(ChatColor.GREEN + "Le plugin a été rechargé avec succès.");
    }

    @Subcommand("cast")
    @Usage("cast <skill>")
    @CommandPermission("yxmaskill.command.cast")
    @Description("Cast any skill without cooldown.")
    @AutoComplete("@skills")
    public void onCast(BukkitCommandActor actor, Skill skill) {

        if (skill == null) {
            actor.reply(ChatColor.RED + "Ce skill n'existe pas.");
            return;
        }

        final GamePlayer invoker = GamePlayer.getGamePlayer(actor.requirePlayer());
        plugin.getSkillManager().cast(invoker, skill, true);
    }

    @Subcommand("changeclass")
    @Usage("changeclass <class>")
    @CommandPermission("yxmaskill.command.changeclass")
    @Description("Change class anytime you want.")
    public void onChangeClass(BukkitCommandActor actor, @Optional ClassType type, @Optional Player player) {

        if (type == null) {
            GuiManager.open(actor.requirePlayer(), ChangeClassGui.class);
            return;
        }

        final GamePlayer invoker = GamePlayer.getGamePlayer(actor.requirePlayer());

        if(invoker.getClassType() == type) {
            invoker.getPlayer().sendMessage(ChatColor.RED + "Vous avez déjà cette classe.");
        } else {
            invoker.setClassType(type);
            invoker.getPlayer().sendMessage(ChatColor.GREEN + "Vous venez de changer de classe !");
        }
    }

    @Subcommand("setlevel")
    @Usage("setlevel <level> [<target>]")
    @CommandPermission("yxmaskill.command.setlevel")
    @Description("Change level of any player.")
    public void onSetLevel(BukkitCommandActor actor, int level, @Optional GamePlayer target) {

        if(level < 1) level = 1;
        if(level > 30) level = 30;

        if(target == null) {
            target = GamePlayer.getGamePlayer(actor.requirePlayer());
        }

        target.setLevel(level);
        target.setExp(0);
        Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(target));

        actor.reply(ChatColor.GREEN + "Vous venez de changer le niveau du joueur: " + target.getPlayer().getName());
    }
}
