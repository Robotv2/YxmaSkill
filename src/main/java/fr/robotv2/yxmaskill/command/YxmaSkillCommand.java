package fr.robotv2.yxmaskill.command;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import org.bukkit.ChatColor;
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
}
