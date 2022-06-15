package fr.robotv2.yxmaskill;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr.robotv2.yxmaskill.player.GamePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.UUID;

public class DataManager {

    private ConnectionSource source;
    private Dao<GamePlayer, UUID> gamePlayerDao;

    protected void initialize(@NotNull ConnectionSource source) throws SQLException {
        this.source = source;
        this.gamePlayerDao = DaoManager.createDao(source, GamePlayer.class);
        TableUtils.createTableIfNotExists(source, GamePlayer.class);
    }

    protected void closeConnection() {
        this.source.closeQuietly();
    }

    @Nullable
    public GamePlayer getGamePlayer(@NotNull UUID playerUUID) {
        try {
            return this.gamePlayerDao.queryForId(playerUUID);
        } catch (SQLException e) {
            YxmaSkill.getInstance().getLogger().warning("Couldn't query data for uuid: " + playerUUID);
            return null;
        }
    }

    public void saveGamePlayer(@NotNull GamePlayer data) {
        try {
            this.gamePlayerDao.createOrUpdate(data);
        } catch (SQLException e) {
            YxmaSkill.getInstance().getLogger().warning("Couldn't save data for uuid:  " + data.getUniqueId());
        }
    }
}
