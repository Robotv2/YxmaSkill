package fr.robotv2.yxmaskill.skill;

import fr.robotv2.yxmaskill.YxmaSkill;
import fr.robotv2.yxmaskill.classes.ClassType;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public abstract class Skill implements Listener {

    public static final NamespacedKey SKILL_KEY = new NamespacedKey(YxmaSkill.getInstance(), "yxma-skill");

    private final YxmaSkill plugin = YxmaSkill.getInstance();

    private final String id;
    private final int cooldown;
    private final ClassType classType;

    private final ConfigurationSection section;
    private final String display;
    private final List<String> lore;

    public Skill(String id, ClassType type, int cooldown) {
        this.id = id;
        this.cooldown = cooldown;
        this.classType = type;
        this.section = plugin.getSkillConfiguration().get().getConfigurationSection("skills." + id);
        assert section != null : "The configuration section for" + id  + "doesn't exist.";
        this.display = this.section.getString("display");
        this.lore = this.section.getStringList("description");
    }

    public Skill(String id, ClassType type) {
        this(id, type, 30);
    }

    /**
     * trigger whenever a player cast the skill.
     */
    public abstract boolean execute(GamePlayer invoker);

    public Skill registerListener() {
        Bukkit.getPluginManager().registerEvents(this, YxmaSkill.getInstance());
        return this;
    }

    public String getId() {
        return id;
    }

    public int getCooldown() {
        return cooldown;
    }

    public ClassType getRequiredClass() {
        return classType;
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public YxmaSkill getPlugin() {
        return plugin;
    }

    public ItemStack getItem(GamePlayer gamePlayer) {

        final ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        final ItemMeta meta = item.getItemMeta();

        final List<String> finalLore = new ArrayList<>(lore);
        finalLore.add("");
        finalLore.add("&8> &7Niveau actuel: &b" + gamePlayer.getSkillLevel(this));

        meta.setDisplayName(ColorUtil.colorize(display));
        meta.setLore(finalLore.stream().map(ColorUtil::colorize).toList());
        meta.getPersistentDataContainer().set(SKILL_KEY, PersistentDataType.STRING, id);

        item.setItemMeta(meta);
        return item;
    }
}
