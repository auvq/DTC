package me.itsenderrr.destroythecore;

import lombok.Getter;
import me.itsenderrr.destroythecore.commands.MainCommand;
import me.itsenderrr.destroythecore.commands.tabcompleters.MainCompleter;
import me.itsenderrr.destroythecore.listeners.EventListener;
import me.itsenderrr.destroythecore.placeholderapi.RegisterHookups;
import me.itsenderrr.destroythecore.rewards.RewardsGUI;
import me.itsenderrr.destroythecore.utils.CC;
import me.itsenderrr.destroythecore.utils.MsgSystem;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private boolean isActive;
    @Getter
    private static Main instance;
    public static HashMap<UUID,Integer> minedBlocks;
    public EventScheduler eventScheduler;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            instance = this;
            eventScheduler = new EventScheduler(this.getConfig());
            eventScheduler.load();
            loadConfigs();
            minedBlocks = new HashMap<>();
            getServer().getConsoleSender().sendMessage(CC.color("&cDTC&a has successfully enabled!"));
            getServer().getPluginManager().registerEvents(new EventListener(), this);
            getServer().getPluginManager().registerEvents(new RewardsGUI(54, "RewardsGUI"), this);
            getCommand("dtc").setExecutor(new MainCommand());
            getCommand("dtc").setTabCompleter(new MainCompleter());


            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                new RegisterHookups().register();
                getLogger().info("Registered PlaceholderAPI expansions for your plugin.");
            } else {
                getLogger().warning("PlaceholderAPI is not installed, PlaceholderAPI expansions will not be available.");
            }
        }, 40);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(CC.color("&cDTC has successfully disabled!"));
    }
    public void setEventState(boolean isStarted){
        isActive = isStarted;
        Location blockLocation = new Location(getBukkitWorld(),
                getConfig().getDouble("Core.Location.x"),
                getConfig().getDouble("Core.Location.y"),
                getConfig().getDouble("Core.Location.z"));

        if(isStarted){
            blockLocation.getBlock().setType(Material.valueOf(getConfig().getString("Core.CoreMaterial")));

            List<String> startMessages = getConfig().getStringList("Messages.StartMessages");
            startMessages.forEach(string ->
                    MsgSystem.sendMessage(CC.color(string).
                            replaceAll("%countdown%", CC.formatTime(eventScheduler.getTimeUntilNextEvent()))));

            Bukkit.getOnlinePlayers().forEach(player -> minedBlocks.put(player.getUniqueId(), 0));
        } else {
            minedBlocks.clear();
            Bukkit.getScheduler().runTaskLater(this, () -> blockLocation.getBlock().setType(Material.valueOf(getConfig().getString("Core.CoreMaterial"))), 100);
        }
    }
    private World getBukkitWorld() {
        final World world = Bukkit.getWorld(getConfig().getString("Core.Location.world"));
        if (world != null) {
            return world;
        }
        return WorldCreator.name(getConfig().getString("Core.Location.world")).createWorld();
    }
    public void loadConfigs() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getConsoleSender().sendMessage(CC.color("&aThe config setup correctly!"));
    }

    @Override
    public void reloadConfig(){
        super.reloadConfig();
        eventScheduler = new EventScheduler(this.getConfig());
        eventScheduler.load();
    }
    public boolean getEventState(){
        return isActive;
    }
}
