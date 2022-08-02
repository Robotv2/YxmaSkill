package fr.robotv2.yxmaskill.util.rpgutil;

import fr.robotv2.yxmaskill.util.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class LevelUtil {

    public static final int LEVEL_MAX = 30;

    private static double X;
    private static int Y;
    private static double Z;
    private static double W;

    public static double CHARAC_RESISTANCE;
    public static double CHARAC_DEXTERITY;
    public static double CHARAC_AGILITY;
    public static double CHARAC_PRECISION;
    public static double CHARAC_STRENGTH;

    public static void loadConstant(Config config) {
        final FileConfiguration configuration = config.get();
        X = configuration.getDouble("level-constant.X");
        Y = configuration.getInt("level-constant.Y");
        Z = configuration.getDouble("level-constant.Z");
        W = configuration.getDouble("level-constant.W");

        CHARAC_RESISTANCE = configuration.getDouble("characteristic.resistance");
        CHARAC_DEXTERITY = configuration.getDouble("characteristic.dexterity");
        CHARAC_AGILITY = configuration.getDouble("characteristic.agility");
        CHARAC_PRECISION = configuration.getDouble("characteristic.precision");
        CHARAC_STRENGTH = configuration.getDouble("characteristic.strength");
    }

    public static double requiredExp(int level) {
        return X * Math.pow((level + Y), 2) + Z * level + W;
    }
}
