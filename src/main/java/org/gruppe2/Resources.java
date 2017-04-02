package org.gruppe2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A helper class with static methods for finding various resources.
 *
 * This differs from {@link org.gruppe2.ui.UIResources} in that it doesn't import any JavaFX stuff,
 * so that the master server can run on headless servers.
 */
public class Resources {
    private final static String pokerProDir = "PokerPro16" + File.separator;

    /**
     * Get the user-writable directory for PokerPro16
     *
     * @return Path to the user directory with a trailing slash
     */
    public static String getUserDir() {
        if (System.getProperty("os.name").equals("Linux")) {
            // Use the XDG Standard when on Linux
            String home = System.getenv("XDG_DATA_HOME");

            if (home == null || home.isEmpty()) {
                home = System.getenv("HOME");

                if (home == null || home.isEmpty()) {
                    // we're screwed
                    return getDefaultUserDir();
                }

                home += "/.local/share";
            }

            return home + "/" + pokerProDir;
        }

        return getDefaultUserDir();
    }

    /**
     * Get the dir subdirectory of the user-writable directory.
     * @param dir directory name
     * @return Path to the user directory with a trailing slash
     */
    public static String getUserDir(String dir) {
        String path = getUserDir() + dir + File.separator;

        new File(path).mkdirs();

        return path;
    }

    /**
     * Get the array of available avatars
     * @return array of avatar names
     */
    public static String[] listAvatars() {
        List<String> avatars = new ArrayList<>();

        Scanner dir = new Scanner(Resources.class.getResourceAsStream("/images/avatars/avatars.txt"));

        while (dir.hasNext()) {
            avatars.add(dir.next());
        }
        dir.close();
        return avatars.toArray(new String[avatars.size()]);
    }

    /**
     * Finds and optionally creates the properties file on disk.
     *
     * @return a File instance for the properties file.
     */
    public static File getProperties() throws IOException {
        new File(getUserDir()).mkdirs();

        File file = new File(getUserDir() + "properties.cfg");

        if (!file.exists()) {
            Main.setFirstStart(true);
            file.createNewFile();
        }

        return file;
    }

    /**
     * Get the default user directory as used by Java.
     *
     * Used by getUserDir to get the user directory in case it can't find the "correct" one by itself.
     * @return Path to the user directory with a trailing slash
     */
    private static String getDefaultUserDir() {
        return System.getProperty("user.home") + File.separator + pokerProDir;
    }

    public static InputStream getDefaultProperties() {
        return Resources.class.getResourceAsStream("/default.properties");
    }
}
