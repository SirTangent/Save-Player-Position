package spigot.savePlayerPosition.project.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import spigot.savePlayerPosition.project.Main;
import spigot.savePlayerPosition.project.Tools.sppDebugger;
import spigot.savePlayerPosition.project.Tools.worldManager;

public class CommandSpp implements CommandExecutor {
    private String titleLabel = ChatColor.DARK_AQUA + "[SPP]" + ": ";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command !");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            showHelp(player);
        } else if (args[0].equalsIgnoreCase("help")) {
            if (args.length != 1) {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Incorrect args amount!");
                return true;
            }
            showHelp(player);
        } else if (args[0].equalsIgnoreCase("version")) {
            if (!(player.hasPermission("spp.*") || player.hasPermission("spp.command.*") || player.hasPermission("spp.command.version"))) {
                player.sendMessage(titleLabel + ChatColor.RED + "You do not have permission:" + ChatColor.YELLOW + " spp.command.version");
                return true;
            }
            if (args.length != 1) {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Incorrect args amount!");
                return true;
            }
            player.sendMessage(titleLabel + ChatColor.DARK_AQUA + "---------------------------");
            player.sendMessage(titleLabel + ChatColor.DARK_AQUA + "Save Player Position v0.1.0");
            player.sendMessage(titleLabel + ChatColor.DARK_AQUA + "---------------------------");
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!(player.hasPermission("spp.*") || player.hasPermission("spp.command.*") || player.hasPermission("spp.command.reload"))) {
                player.sendMessage(titleLabel + ChatColor.RED + "You do not have permission:" + ChatColor.YELLOW + " spp.command.reload");
                return true;
            }
            if (args.length != 1) {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Incorrect args amount!");
                return true;
            }

            reloadConfig();
            player.sendMessage(titleLabel + ChatColor.RESET + "Reloaded the config");
        } else if (args[0].equalsIgnoreCase("setdebug")){
            if (!(player.hasPermission("spp.*") || player.hasPermission("spp.command.*") || player.hasPermission("spp.command.setdebug"))) {
                player.sendMessage(titleLabel + ChatColor.RED + "You do not have permission:" + ChatColor.YELLOW + " spp.command.setdebug");
                return true;
            }
            if (args.length != 2) {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Incorrect args amount!");
                return true;
            }

            if (args[1].equalsIgnoreCase("true")) {
                player.sendMessage(titleLabel + ChatColor.RESET + "Setting debug to true");
                saveConfig(true);
            } else if (args[1].equalsIgnoreCase("false")) {
                player.sendMessage(titleLabel + ChatColor.RESET + "Setting debug to false");
                saveConfig(false);
            } else {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Unknown value!");
            }
        } else if (args[0].equalsIgnoreCase("blacklist")) {
            if (!(player.hasPermission("spp.*") || player.hasPermission("spp.admin.*") || player.hasPermission("spp.admin.blacklist"))) {
                player.sendMessage(titleLabel + ChatColor.RED + "You do not have permission:" + ChatColor.YELLOW + " spp.admin.blacklist");
                return true;
            }
            if (args.length != 3) {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Incorrect args amount!");
                return true;
            }

            if (args[1].equalsIgnoreCase("add")) {
                worldManager.addBlacklistWorld(args[2], player);
            } else if (args[1].equalsIgnoreCase("remove")) {
                worldManager.removeBlacklistWorld(args[2], player);
            } else {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Unknown value!");
            }
        } else  if (args[0].equalsIgnoreCase("group")){
            if (!(player.hasPermission("spp.*") || player.hasPermission("spp.admin.*") || player.hasPermission("spp.admin.group.*")))
            if (!(args.length >= 2)) {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Incorrect args amount!");
                return true;
            }
            if (args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("delete")) {
                if (!(player.hasPermission("spp.*") || player.hasPermission("spp.admin.*") || player.hasPermission("spp.admin.group.*") || player.hasPermission("spp.admin.group.manage"))) {
                    player.sendMessage(titleLabel + ChatColor.RED + "You do not have permission:" + ChatColor.YELLOW + " spp.admin.group.manage");
                    return true;
                }
                if (args.length != 3) {
                    player.sendMessage(titleLabel + ChatColor.RED + "Error: Incorrect args amount!");
                    return true;
                }

                if (args[1].equalsIgnoreCase("create")) {
                    worldManager.createGroup(args[2], player);
                } else {
                    worldManager.deleteGroup(args[2], player);
                }
            } else if (args[1].equalsIgnoreCase("addWorld") || args[1].equalsIgnoreCase("removeWorld")) {
                if (!(player.hasPermission("spp.*") || player.hasPermission("spp.admin.*") || player.hasPermission("spp.admin.group.*") || player.hasPermission("spp.admin.group.manage"))) {
                    player.sendMessage(titleLabel + ChatColor.RED + "You do not have permission:" + ChatColor.YELLOW + " spp.admin.group.manage");
                    return true;
                }
                if (args.length != 4) {
                    player.sendMessage(titleLabel + ChatColor.RED + "Error: Incorrect args amount!");
                    return true;
                }
            } else {
                player.sendMessage(titleLabel + ChatColor.RED + "Error: Unknown command!");
            }
        } else {
            player.sendMessage(titleLabel + ChatColor.RED + "Error: Unknown command!");
        }
        return true;
    }


    private void saveConfig(Boolean debug) {
        JavaPlugin.getPlugin(Main.class).getConfig().set("debug", debug);
        JavaPlugin.getPlugin(Main.class).saveConfig();
        reloadConfig();
    }
    private void reloadConfig() {
        sppDebugger.log("Reloading config...");
        JavaPlugin.getPlugin(Main.class).reloadConfig();
        sppDebugger.setDebug(JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("debug"));
    }

    private void showHelp(Player player) {
        player.sendMessage(titleLabel + ChatColor.DARK_AQUA + "---" + ChatColor.RED + "Save Player Position" + ChatColor.DARK_AQUA + "---");
        player.sendMessage(titleLabel + ChatColor.GREEN + "/spp help " + ChatColor.RESET + "- Shows this page");
        player.sendMessage(titleLabel + ChatColor.GREEN + "/spp version " + ChatColor.RESET + "- Shows the plugin version");
        player.sendMessage(titleLabel + ChatColor.GREEN + "/spp reload " + ChatColor.RESET + "- Reloads the config");
        player.sendMessage(titleLabel + ChatColor.GREEN + "/spp setdebug " + "<bool> " + ChatColor.RESET + "- Sets the debug value");
        player.sendMessage(titleLabel + ChatColor.GREEN + "/spp blacklist " + "[add/remove] <world> " + ChatColor.RESET + "- Adds/removes a world from the blacklist");
        player.sendMessage(titleLabel + ChatColor.DARK_AQUA + "---" + ChatColor.RED + "Page 1 of 1" + ChatColor.DARK_AQUA + "---");
    }
}
