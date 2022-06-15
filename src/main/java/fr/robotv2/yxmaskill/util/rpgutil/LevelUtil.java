package fr.robotv2.yxmaskill.util.rpgutil;

import fr.robotv2.yxmaskill.util.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class LevelUtil {

    public static final int LEVEL_MAX = 30;

    private static double X;
    private static int Y;
    private static double Z;
    private static double W;

    public static void loadConstant(Config config) {
        final FileConfiguration configuration = config.get();
        X = configuration.getDouble("level-constant.X");
        Y = configuration.getInt("level-constant.Y");
        Z = configuration.getDouble("level-constant.Z");
        W = configuration.getDouble("level-constant.W");
    }

    public static double requiredExp(int level) {
        return X * Math.pow((level + Y), 2) + Z * level + W;
    }
}
