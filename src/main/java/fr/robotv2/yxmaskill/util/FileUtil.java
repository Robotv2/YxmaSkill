package fr.robotv2.yxmaskill.util;

import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File createFile(String path, String fileName) {
        return createFile(new File(path, fileName));
    }

    public static File createFile(File file) {

        if(file.exists()) {
            return file;
        }

        try {
            if(!file.getParentFile().exists()) {
                file.mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
