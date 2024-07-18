package me.itsenderrr.destroythecore.rewards;

import java.io.IOException;
import java.util.Objects;
import me.itsenderrr.destroythecore.Main;
import me.itsenderrr.destroythecore.utils.Base64Util;
import me.itsenderrr.destroythecore.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RewardsGUI implements Listener {
    private Main plugin = Main.getInstance();

    private ItemStack[] itemStacks;

    private final int slots;

    private final String title;

    private Inventory inventory;

    public RewardsGUI(int slots, String title) {
        this.slots = slots;
        this.title = title;
        createGUI();
    }

    private void createGUI() {
        try {
            this.inventory = Base64Util.fromBase64(Objects.<String>requireNonNull(this.plugin.getConfig().getString("RewardsGUI.base64")), "RewardsGUI");
        } catch (IOException ignored) {
            this.plugin.getServer().getConsoleSender().sendMessage(CC.color("&cThe config couldn't detect a base64 GUI, reverting to default."));
            createDefaultGUI(this.slots, this.title);
        }
    }

    public void createDefaultGUI(int slots, @NotNull String title) {
        this.inventory = Bukkit.getServer().createInventory(null, slots, CC.color(title));
        this.plugin.getConfig().set("RewardsGUI.base64", Base64Util.toBase64(this.inventory));
    }

    public void openGUI(Player player) {
        if (this.inventory == null)
            createGUI();
        player.openInventory(this.inventory);
    }

    public void saveGUI() {
        Base64Util.toBase64(this.inventory);
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains(this.title) || event.getView().getTitle().equalsIgnoreCase(this.title)) {
            this.inventory = event.getInventory();
            this.plugin.getConfig().set("RewardsGUI.base64", Base64Util.toBase64(this.inventory));
            this.plugin.saveConfig();
            event.getPlayer().sendMessage(CC.color("&aInventory successfully saved!"));
        }
    }

    @EventHandler
    public void inventoryOpen(InventoryOpenEvent event) {
        createGUI();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if ((event.getView().getTitle().contains("RewardsGUI") || event.getView().getTitle().equalsIgnoreCase("RewardsGUI")) &&
                event.getClickedInventory() != player.getInventory())
            if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                player.sendMessage(CC.color("&aSuccessfully added something to the GUI!"));
                this.inventory.setItem(event.getSlot(), event.getCursor());
            } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                this.inventory.setItem(event.getSlot(), new ItemStack(Material.AIR));
                event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                event.getWhoClicked().sendMessage(CC.color("&cYou've removed the item from the rewards!"));
                player.updateInventory();
                event.setCancelled(true);
            }
    }
}
