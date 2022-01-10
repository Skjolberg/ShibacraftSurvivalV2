package net.shibacraft.shibacraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompletionPresident implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        arguments.clear();
        arguments.add("add");
        arguments.add("remove");
        arguments.add("list");

        List<String> result = new ArrayList<String>();

        if(args.length == 1){
            for(String i : arguments){
                if(i.toLowerCase().startsWith(args[0].toLowerCase())){
                    result.add(i);
                }
            }
            return result;
        } else if(args[0].equalsIgnoreCase("add") & args.length == 2){
            arguments.clear();
            arguments.add("user");
            arguments.add("city");
            for(String i : arguments){
                if(i.toLowerCase().startsWith(args[1].toLowerCase())){
                    result.add(i);
                }
            }
            return result;
        } else if(args[0].equalsIgnoreCase("remove") & args.length == 2){
            arguments.clear();
            arguments.add("user");
            for(String i : arguments){
                if(i.toLowerCase().startsWith(args[1].toLowerCase())){
                    result.add(i);
                }
            }
            return result;
        }
        return null;
    }
}
