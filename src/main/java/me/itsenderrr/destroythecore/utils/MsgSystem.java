package me.itsenderrr.destroythecore.utils;

import me.itsenderrr.destroythecore.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MsgSystem {

    public static char HEART = '\u2764';

    public static void sendMessage(String text) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(CC.color(text)));
        MsgSystem.logConsole(text);
    }

    public static void log(CommandSender sender, String text) {
        Command.broadcastCommandMessage(sender, text);
    }

    public static void logConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(CC.color(text));
    }

}