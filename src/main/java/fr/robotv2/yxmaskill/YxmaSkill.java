package fr.robotv2.yxmaskill;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import fr.robotv2.yxmaskill.command.YxmaSkillCommand;
import fr.robotv2.yxmaskill.skill.Skill;
import fr.robotv2.yxmaskill.skill.SkillManager;
import fr.robotv2.yxmaskill.skill.epeiste.CircleSkill;
import fr.robotv2.yxmaskill.skill.epeiste.DashSkill;
import fr.robotv2.yxmaskill.util.FileUtil;
import fr.robotv2.yxmaskill.util.config.Config;
import fr.robotv2.yxmaskill.util.config.ConfigAPI;
import fr.robotv2.yxmaskill.util.rpgutil.LevelUtil;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.sql.SQLException;
import java.util.Locale;

public final class YxmaSkill extends JavaPlugin {

    private static YxmaSkill instance;
    private final SkillManager skillManager = new SkillManager();
    private final DataManager dataManager = new DataManager();

    public static YxmaSkill getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.loadDataManager();
        this.loadSkills();
        this.loadCommands();

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
        LevelUtil.loadConstant(getLevelConfiguration());
    }

    // <<- GETTERS ->>

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public DataManager getDataManager() {
        return dataManager;
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
        //EPEISTE
        getSkillManager().registerSkill(new DashSkill());
        getSkillManager().registerSkill(new CircleSkill());
    }

    private void loadCommands() {
        final BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        handler.setLocale(Locale.FRANCE);
        handler.registerValueResolver(Skill.class, context -> getSkillManager().getSkill(context.pop()));
        handler.getAutoCompleter().registerSuggestion("skills", getSkillManager().getSkillsId());
        handler.register(new YxmaSkillCommand(this));
    }

    // <<- CONFIGURATION ->>

    public Config getSkillConfiguration() {
        return ConfigAPI.getConfig("skills");
    }

    public Config getLevelConfiguration() {
        return ConfigAPI.getConfig("levels");
    }
}
