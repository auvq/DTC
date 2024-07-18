package me.itsenderrr.destroythecore.commands;

import me.itsenderrr.destroythecore.Main;
import me.itsenderrr.destroythecore.rewards.RewardsGUI;
import me.itsenderrr.destroythecore.utils.Base64Util;
import me.itsenderrr.destroythecore.utils.CC;
import me.itsenderrr.destroythecore.utils.MsgSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static me.itsenderrr.destroythecore.rewards.RollReward.rollReward;

public class MainCommand implements CommandExecutor {


    private RewardsGUI rewardsGUI;
    private Main plugin = Main.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(cmd.getName().equalsIgnoreCase("dtc") || cmd.getName().equalsIgnoreCase("destroythecore")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(args.length == 1) {
                    if (player.hasPermission("dtc.admin")) {
                        if (args[0].equalsIgnoreCase("help")) {
                            sendHelpMessage(player);
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("start")) {
                            plugin.setEventState(true);
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("stop")) {
                            List<String> stoppedMessages = plugin.getConfig().getStringList("Messages.StoppedMessages");
                            stoppedMessages.forEach(string ->
                                    MsgSystem.sendMessage(CC.color(string).
                                            replaceAll("%countdown%", String.valueOf(CC.formatTime(plugin.eventScheduler.getTimeUntilNextEvent())))));
                            plugin.setEventState(false);
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("setblock")) {
                            player.sendMessage(CC.color("&cUsage: /dtc setblock X Y Z"));
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("reload")) {
                            plugin.reloadConfig();
                            player.sendMessage(CC.color("&aThe config has been successfully reloaded!"));
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("rewards")) {
                            try {
                                Inventory inventory = Base64Util.fromBase64(Objects.<String>requireNonNull(this.plugin.getConfig().getString("RewardsGUI.base64")),
                                        Objects.<String>requireNonNull(this.plugin.getConfig().getString("RewardsGUI.name")));
                                InventoryHolder holder = inventory.getHolder();
                                this.rewardsGUI = new RewardsGUI(inventory.getSize(), this.plugin.getConfig().getString("RewardsGUI.name"));
                                player.sendMessage(CC.color("&aSuccessfully loaded the gui"));
                                this.rewardsGUI.openGUI(player);
                                return true;
                            } catch (IOException ignored) {
                                player.sendMessage("&cRan into an exception, please check the console.");
                            }
                            return true;
                        }
                    }
                    if(args[0].equalsIgnoreCase("duration")){
                        player.sendMessage(CC.color("&eThe next event will be in: &6" + CC.formatTime(plugin.eventScheduler.getTimeUntilNextEvent())));
                    }
                    } else if (args.length == 4) {
                        if (args[0].equalsIgnoreCase("setblock")) {
                            player.sendMessage(CC.color("&aSuccessfully set a new core!"));
                            plugin.getConfig().set("Core.Location.world", player.getWorld().getName());
                            plugin.getConfig().set("Core.Location.x", Integer.parseInt(args[1]));
                            plugin.getConfig().set("Core.Location.y", Integer.parseInt(args[2]) - 1);
                            plugin.getConfig().set("Core.Location.z", Integer.parseInt(args[3]));
                            plugin.loadConfigs();
                            return true;
                        }
                    } else {
                        sendHelpMessage(player);
                    }
            }
            }
        return false;
    }

    public void sendHelpMessage(Player player){
        if(player.hasPermission("dtc.admin")){
            player.sendMessage(CC.color("&e--------------"));
            player.sendMessage(CC.color("&e- /dtc duration &6- shows when is the next DTC"));
            player.sendMessage(CC.color("&e- /dtc start &6- starts the DTC event"));
            player.sendMessage(CC.color("&e- /dtc stop &6- stops the DTC event"));
            player.sendMessage(CC.color("&e- /dtc setblock &6- sets the DTC block"));
            player.sendMessage(CC.color("&e- /dtc reload &6- reloads the config"));
            player.sendMessage(CC.color("&e- /dtc rewards &6- lets you change the winner rewards"));
            player.sendMessage(CC.color("&e--------------"));
        } else {
            player.sendMessage(CC.color("&e--------------"));
            player.sendMessage(CC.color("&e- /dtc duration &6- shows when is the next DTC"));
            player.sendMessage(CC.color("&e--------------"));
        }
    }
}
