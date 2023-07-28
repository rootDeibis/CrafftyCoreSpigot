package me.rootdeibis.crafftty.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.Method;
import java.util.List;

public class TabCompleteExecutor implements TabCompleter {
    public TabCompleteExecutor(Method[] completion_methods) {

    }
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    private
}
