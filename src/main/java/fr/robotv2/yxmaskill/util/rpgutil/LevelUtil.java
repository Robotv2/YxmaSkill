package fr.robotv2.yxmaskill.util.rpgutil;

import fr.robotv2.yxmaskill.util.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class LevelUtil {

    public static final int LEVEL_MAX = 30;

    private static double X;
    private static double Y;
    private static double Z;

    public static void loadConstant(Config config) {
        final FileConfiguration configuration = config.get();
        X = configuration.getDouble("level-constant.X");
        Y = configuration.getDouble("level-constant.Y");
        Z = configuration.getDouble("level-constant.Z");
    }

    // (x * niveau^2) + (y * niveau) + z

    public static double requiredExp(int level) {
        return (X * (level^2)) + (Y * level) + Z;
    }
}
