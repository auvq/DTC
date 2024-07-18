package me.itsenderrr.destroythecore.listeners;

import me.itsenderrr.destroythecore.Main;
import me.itsenderrr.destroythecore.utils.CC;
import me.itsenderrr.destroythecore.utils.MsgSystem;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.itsenderrr.destroythecore.Main.minedBlocks;
import static me.itsenderrr.destroythecore.rewards.RollReward.rollReward;

public class EventListener implements Listener {


    private static Main plugin = Main.getInstance();
    private FileConfiguration config = plugin.getConfig();
    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Location blockLocation = new Location(getBukkitWorld(),
                config.getDouble("Core.Location.x"),
                config.getDouble("Core.Location.y"),
                config.getDouble("Core.Location.z"));

        if (event.getBlock().getLocation().equals(blockLocation)) {
            if (plugin.getEventState()) {
                Player player = event.getPlayer();
                if (minedBlocks.get(player.getUniqueId()) + 1 == config.getInt("Core.CoreHealth")) {
                    plugin.setEventState(false);
                    List<String> winnerMessages = plugin.getConfig().getStringList("Messages.WinnerMessages");
                    winnerMessages.forEach(string ->
                            MsgSystem.sendMessage(CC.color(string).
                                    replaceAll("%countdown%", String.valueOf(CC.formatTime(plugin.eventScheduler.getTimeUntilNextEvent())))
                                    .replaceAll("%player%", player.getDisplayName())));
                    rollReward(player);
                } else {
                        event.setCancelled(true);
                        minedBlocks.merge(player.getUniqueId(), 1, Integer::sum);
                    }
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.color("&cThe event hasn't started yet!"));
                }
            }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(plugin.getEventState()){
            if(minedBlocks.containsKey(event.getPlayer().getUniqueId())){
                return;
            } else {
                minedBlocks.put(event.getPlayer().getUniqueId(), 0);
            }
        }
    }
    public static int getHealthLeft(Player player){
        return plugin.getConfig().getInt("Core.CoreHealth") - minedBlocks.get(player.getUniqueId());
    }
    private World getBukkitWorld() {
        final World world = Bukkit.getWorld(plugin.getConfig().getString("Core.Location.world"));
        if (world != null) {
            return world;
        }
        return WorldCreator.name(plugin.getConfig().getString("Core.Location.world")).createWorld();
    }
}
