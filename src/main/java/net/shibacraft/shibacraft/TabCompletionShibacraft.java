package net.shibacraft.shibacraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompletionShibacraft implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        arguments.clear();
        arguments.add("reload");
        arguments.add("city");

        List<String> result = new ArrayList<String>();

        if(args.length == 1){
            for(String i : arguments){
                if(i.toLowerCase().startsWith(args[0].toLowerCase())){
                    result.add(i);
                }
            }
            return result;
        } else if(args[0].equalsIgnoreCase("city") & args.length == 2){
            arguments.clear();
            arguments.add("delete");
            for(String i : arguments){
                if(i.toLowerCase().startsWith(args[1].toLowerCase())){
                    result.add(i);
                }
            }
            return result;
        }else if(args[1].equalsIgnoreCase("delete") & args.length == 3){
            arguments.clear();
            arguments.add("all");
            for(String i : arguments){
                if(i.toLowerCase().startsWith(args[2].toLowerCase())){
                    result.add(i);
                }
            }
            return result;
        }
        return null;
    }

}
