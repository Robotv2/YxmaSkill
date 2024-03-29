package fr.robotv2.yxmaskill;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import de.slikey.effectlib.EffectManager;
import fr.robotv2.yxmaskill.command.YxmaSkillCommand;
import fr.robotv2.yxmaskill.listeners.*;
import fr.robotv2.yxmaskill.listeners.classes.EpeisteListener;
import fr.robotv2.yxmaskill.listeners.classes.NoneListener;
import fr.robotv2.yxmaskill.listeners.classes.PugilisteListener;
import fr.robotv2.yxmaskill.listeners.classes.SniperListener;
import fr.robotv2.yxmaskill.player.GamePlayer;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.skill.SkillManager;
import fr.robotv2.yxmaskill.skill.epeiste.CircleSkill;
import fr.robotv2.yxmaskill.skill.epeiste.ConeSkill;
import fr.robotv2.yxmaskill.skill.epeiste.DashSkill;
import fr.robotv2.yxmaskill.ui.GuiManager;
import fr.robotv2.yxmaskill.ui.stock.ChangeClassGui;
import fr.robotv2.yxmaskill.util.FileUtil;
import fr.robotv2.yxmaskill.util.config.Config;
import fr.robotv2.yxmaskill.util.config.ConfigAPI;
import fr.robotv2.yxmaskill.util.rpgutil.LevelUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.sql.SQLException;
import java.util.Locale;

public final class YxmaSkill extends JavaPlugin {

    private static YxmaSkill instance;
    private final SkillManager skillManager = new SkillManager();
    private final DataManager dataManager = new DataManager();
    private final EffectManager effectManager = new EffectManager(this);

    public static YxmaSkill getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        ConfigAPI.init(this);
        getSkillConfiguration().setup();
        getLevelConfiguration().setup();
        getClassConfiguration().setup();

        this.loadDataManager();
        this.loadSkills();
        this.loadListeners();
        this.loadCommands();
        this.loadGuis();

        LevelUtil.loadConstant(getLevelConfiguration());
    }

    @Override
    public void onDisable() {
        getDataManager().closeConnection();
        instance = null;
    }

    public void onReload() {

        getSkillConfiguration().reload();
        getLevelConfiguration().reload();
        getClassConfiguration().reload();
        LevelUtil.loadConstant(getLevelConfiguration());

        this.loadSkills();
    }

    // <<- GETTERS ->>

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    // <<- LOADERS ->>

    private void loadDataManager() {
        try {
            final File file = FileUtil.createFile(getDataFolder().getPath(), "database.db");
            final String databaseURL = "jdbc:sqlite:".concat(file.getPath());
            final ConnectionSource connectionSource = new JdbcConnectionSource(databaseURL);
            this.dataManager.initialize(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().warning("Couldn't connect to the database. Shutting down the plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void loadSkills() {
        getSkillManager().clearSkills();
        //EPEISTE
        getSkillManager().registerSkill(new DashSkill());
        getSkillManager().registerSkill(new ConeSkill());
        getSkillManager().registerSkill(new CircleSkill());
    }

    private void loadListeners() {
        final PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new ExpChangeListener(), this);
        pm.registerEvents(new LevelUpListener(), this);
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new SkillBookListener(this), this);

        pm.registerEvents(new EpeisteListener(), this);
        pm.registerEvents(new PugilisteListener(), this);
        pm.registerEvents(new SniperListener(), this);
        pm.registerEvents(new NoneListener(), this);
    }

    private void loadCommands() {
        final BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        handler.setLocale(Locale.FRANCE);

        handler.registerValueResolver(Skill.class, context -> getSkillManager().getSkill(context.pop()));
        handler.registerValueResolver(GamePlayer.class, context -> {
            final Player player = Bukkit.getPlayer(context.pop());
            return player != null ? GamePlayer.getGamePlayer(player) : null;
        });

        handler.getAutoCompleter().registerSuggestion("skills", getSkillManager().getSkillsId());
        handler.register(new YxmaSkillCommand(this));
    }

    private void loadGuis() {
        getServer().getPluginManager().registerEvents(new GuiManager(), this);
        GuiManager.addMenu(new ChangeClassGui());
    }

    // <<- CONFIGURATION ->>

    public Config getSkillConfiguration() {
        return ConfigAPI.getConfig("skills");
    }

    public Config getLevelConfiguration() {
        return ConfigAPI.getConfig("levels");
    }

    public Config getClassConfiguration() {
        return ConfigAPI.getConfig("class");
    }
}
