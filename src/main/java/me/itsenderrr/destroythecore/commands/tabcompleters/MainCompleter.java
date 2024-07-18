package me.itsenderrr.destroythecore.commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if(sender instanceof Player){
            if(args.length == 1){
                    list.add("start");
                    list.add("stop");
                    list.add("setblock");
                    list.add("duration");
                    list.add("help");
                    list.add("reload");
                    list.add("rewards");
                    return list;
                }
                if(args[0].equalsIgnoreCase("setblock")){
                    if(args.length == 2){
                        int x = (int) ((Player) sender).getLocation().getX();
                        list.add(String.valueOf(x));
                        return list;
                    }
                    if(args.length == 3){
                        int y = (int) ((Player) sender).getLocation().getY();
                        list.add(String.valueOf(y));
                        return list;
                    }
                    if(args.length == 4){
                        int z = (int) ((Player) sender).getLocation().getZ();
                        list.add(String.valueOf(z));
                        return list;
                    }
                }
            }
        return null;
    }
}
