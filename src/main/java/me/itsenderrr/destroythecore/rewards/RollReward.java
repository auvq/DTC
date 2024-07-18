package me.itsenderrr.destroythecore.rewards;

import me.itsenderrr.destroythecore.Main;
import me.itsenderrr.destroythecore.utils.Base64Util;
import me.itsenderrr.destroythecore.utils.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class RollReward {


    private static Inventory rewardsGUI;

    private static Main plugin = Main.getInstance();


    private static void loadGUI(){
        try {
            rewardsGUI = Base64Util.fromBase64(Objects.requireNonNull(plugin.getConfig().getString("RewardsGUI.base64")), CC.color("RewardsGUI"));
            plugin.getServer().getConsoleSender().sendMessage(CC.color("&aSuccessfully loaded the gui!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(rewardsGUI.getItem(0) == null) {
            plugin.getServer().getConsoleSender().sendMessage("&cThe rewards menu is EMPTY! This might cause some issues.");
        } else if(Objects.requireNonNull(rewardsGUI.getItem(0)).getType() == Material.AIR) {
            plugin.getServer().getConsoleSender().sendMessage("&cThe rewards menu is EMPTY! This might cause some issues.");
        }
    }
    public static void rollReward(Player player) {
        loadGUI();
        if(rewardsGUI.getItem(0) == null) {
            plugin.getServer().getConsoleSender().sendMessage("&cThe rewards menu is EMPTY! This might cause some issues.");
        } else if(Objects.requireNonNull(rewardsGUI.getItem(0)).getType() == Material.AIR) {
            plugin.getServer().getConsoleSender().sendMessage("&cThe rewards menu is EMPTY! This might cause some issues.");
        } else {
        int rewardCount = 0;
        for (ItemStack item : rewardsGUI) {
            if (item == null) {
                continue;
            }
            if (item.getType() == Material.AIR) {
                continue;
            } else {
                rewardCount++;
            }
        }
            Random random = new Random();
            int chosenReward = random.nextInt(rewardCount);
            player.getInventory().addItem(Objects.requireNonNull(rewardsGUI.getItem(chosenReward)));
        }
    }
}
