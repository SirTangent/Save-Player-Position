package spigot.savePlayerPosition.project.Tools;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import spigot.savePlayerPosition.project.Main;

import java.io.*;

public class playerDataManager {
    private static final File userData = new File(JavaPlugin.getPlugin(Main.class).getDataFolder(), "playerData");
    private static final File userWorldData = new File(userData, "worldData");
    private static final File userGroupData = new File(userData, "groupData");
    private static final File userGroupFirstData = new File(userData, "groupFirstData");
    private static final String strClass = "PlayerDataManager";

    public static void enablePlayerMan() {
        userData.mkdir();
        userWorldData.mkdir();
        userGroupData.mkdir();
        userGroupFirstData.mkdir();
    }

    private static boolean checkPlayerWorldFile(String uuid) {
        File tempUser = new File(userWorldData, uuid + ".dat");
        if (!(tempUser.exists())) {
            try {
                return tempUser.createNewFile();
            } catch (IOException e) {
                sppDebugger.forceLog(strClass, "checkPlayerWorldFile","Error: Issue creating new player file!", ChatColor.RED);
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public static void saveWorldData(String uuid, String key, double xValue, double yValue, double zValue) {
        checkPlayerWorldFile(uuid);
        String strMethod = "saveWorldData";
        try {
            sppDebugger.log(strClass, strMethod,"Checking file for world data of \"" + key + "\"...");
            BufferedReader file = new BufferedReader(new FileReader(new File(userWorldData, uuid + ".dat")));
            String line = file.readLine();
            StringBuilder input = new StringBuilder();
            File tempUser = new File(userWorldData, uuid + ".dat.tmp");
            tempUser.createNewFile();
            BufferedWriter tempFile = new BufferedWriter(new FileWriter(tempUser));
            boolean found = false;

            while (line != null) {
                String currWorld = line.split(":")[0];
                if (currWorld.equals(key)) {
                    sppDebugger.log(strClass, strMethod, "Found data for world \"" + key + "\"");
                    input.append(key + ":" + xValue + "," + yValue + "," + zValue).append('\n');
                    found = true;
                } else {
                    input.append(line).append('\n');
                }
                line = file.readLine();
            }
            sppDebugger.log(strClass, strMethod,"Writing data to temp file");
            tempFile.append(input.toString());
            if (!(found)) {
                sppDebugger.log(strClass, strMethod, "Appending world data to end of file");
                tempFile.append(key + ":" + xValue + "," + yValue + "," + zValue + '\n');
            }

            file.close();
            tempFile.close();
            new File(userWorldData, uuid + ".dat").delete();
            new File(userWorldData, uuid + ".dat.tmp").renameTo(new File(userWorldData, uuid + ".dat"));
        } catch (FileNotFoundException e) {
            sppDebugger.forceLog(strClass,strMethod, "Error: Issue finding player file!", ChatColor.RED);
            throw new RuntimeException(e);
        } catch (IOException e) {
            sppDebugger.forceLog(strClass, strMethod, "Error: Issue loading player file!", ChatColor.RED);
            throw new RuntimeException(e);
        }
    }

    public static double[] getWorldData(String uuid, String key) {
        checkPlayerWorldFile(uuid);
        String strMethod = "getWorldData";
        try {
            sppDebugger.log(strClass, strMethod, "Checking file for world data of \"" + key + "\"...");
            BufferedReader file = new BufferedReader(new FileReader(new File(userWorldData, uuid + ".dat")));
            String line = file.readLine();
            while (line != null) {
                String currWorld = line.split(":")[0];
                //sppDebugger.log("Checking key against \"" + currWorld + "\"");
                if (currWorld.equals(key)) {
                    String value = line.split(":")[1];
                    String[] cords = value.split(",");
                    double[] tempReturn = {Double.parseDouble(cords[0]), Double.parseDouble(cords[1]), Double.parseDouble(cords[2])};
                    file.close();
                    sppDebugger.log(strClass, strMethod, "Found world data");
                    return tempReturn;
                }
                line = file.readLine();
            }
            file.close();
        } catch (FileNotFoundException e) {
            sppDebugger.forceLog(strClass, strMethod, "Error: Issue finding player file!", ChatColor.RED);
            throw new RuntimeException(e);
        } catch (IOException e) {
            sppDebugger.forceLog(strClass, strMethod, "Error: Issue loading player file!", ChatColor.RED);
            throw new RuntimeException(e);
        }
        sppDebugger.log(strClass, strMethod, "Did not find world data");
        return null;
    }

    private static boolean checkPlayerGroupFile(String uuid) {
        File tempUser = new File(userGroupData, uuid + ".dat");
        if (!(tempUser.exists())) {
            try {
                return tempUser.createNewFile();
            } catch (IOException e) {
                sppDebugger.forceLog(strClass, "checkPlayerGroupFile", "Error: Issue creating new player file!", ChatColor.RED);
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public static String getGroupData(String uuid, String key) {
        checkPlayerGroupFile(uuid);
        String strMethod = "getGroupData";
        try {
            sppDebugger.log(strClass, strMethod, "Checking file for group data of \"" + key + "\"...");
            BufferedReader file = new BufferedReader(new FileReader(new File(userGroupData, uuid + ".dat")));
            String line = file.readLine();
            while (line != null) {
                String currWorld = line.split(":")[0];
                //sppDebugger.log("Checking key against \"" + currWorld + "\"");
                if (currWorld.equals(key)) {
                    String value = line.split(":")[1];
                    file.close();
                    sppDebugger.log(strClass, strMethod, "Found group data");
                    return value;
                }
                line = file.readLine();
            }
            file.close();
        } catch (FileNotFoundException e) {
            sppDebugger.forceLog(strClass, strMethod, "Error: Issue finding player file!", ChatColor.RED);
            throw new RuntimeException(e);
        } catch (IOException e) {
            sppDebugger.forceLog(strClass, strMethod, "Error: Issue loading player file!", ChatColor.RED);
            throw new RuntimeException(e);
        }
        sppDebugger.log(strClass, strMethod, "Did not find group data");
        return null;
    }

    public static void saveGroupData(String uuid, String key, String value) {
        String strMethod = "saveGroupData";
        checkPlayerGroupFile(uuid);
        try {
            sppDebugger.log(strClass, strMethod, "Checking file for group data of \"" + key + "\"...");
            BufferedReader file = new BufferedReader(new FileReader(new File(userGroupData, uuid + ".dat")));
            String line = file.readLine();
            StringBuilder input = new StringBuilder();
            File tempUser = new File(userGroupData, uuid + ".dat.tmp");
            tempUser.createNewFile();
            BufferedWriter tempFile = new BufferedWriter(new FileWriter(tempUser));
            boolean found = false;

            while (line != null) {
                String currWorld = line.split(":")[0];
                if (currWorld.equals(key)) {
                    sppDebugger.log(strClass, strMethod, "Found data for group \"" + key + "\"");
                    input.append(key + ":" + value).append('\n');
                    found = true;
                } else {
                    input.append(line).append('\n');
                }
                line = file.readLine();
            }
            sppDebugger.log(strClass, strMethod, "Writing data to temp file");
            tempFile.append(input.toString());
            if (!(found)) {
                sppDebugger.log(strClass, strMethod, "Appending group data to end of file");
                tempFile.append(key + ":" + value + '\n');
            }

            file.close();
            tempFile.close();
            new File(userGroupData, uuid + ".dat").delete();
            new File(userGroupData, uuid + ".dat.tmp").renameTo(new File(userGroupData, uuid + ".dat"));
        } catch (FileNotFoundException e) {
            sppDebugger.forceLog(strClass, strMethod, "Error: Issue finding player file!", ChatColor.RED);
            throw new RuntimeException(e);
        } catch (IOException e) {
            sppDebugger.forceLog(strClass, strMethod, "Error: Issue loading player file!", ChatColor.RED);
            throw new RuntimeException(e);
        }
    }
}
