package me.itsenderrr.destroythecore.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.itsenderrr.destroythecore.Main;
import me.itsenderrr.destroythecore.listeners.EventListener;
import me.itsenderrr.destroythecore.utils.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class RegisterHookups extends PlaceholderExpansion {

    private final Main plugin = Main.getInstance();

    @Override
    public @NotNull String getIdentifier() {
        return "dtc"; // Replace with your plugin's identifier
    }

    @Override
    public @NotNull String getAuthor() {
        return "ItsEnderrr";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("blocklivesleft")) {
            return String.valueOf(EventListener.getHealthLeft((Player) player));
        } else if(params.equalsIgnoreCase("timeleft")){
            return CC.formatTime(plugin.eventScheduler.getTimeUntilNextEvent());
        }
        return null;
    }
}