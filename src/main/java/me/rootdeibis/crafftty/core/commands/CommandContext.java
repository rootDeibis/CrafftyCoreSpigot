package me.rootdeibis.crafftty.core.commands;

import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandContext {

    private final CoreCommand command;
    private final Method commandMethod;
    private final Object commandClass;
    private List<SubCommandContext> subCommands = new ArrayList<>();
    public CommandContext(CoreCommand command, Method commandMethod, Class<?> commandClass) {
        this.command = command;
        this.commandMethod = commandMethod;
        this.commandClass = commandClass;
    }

    public CommandContext(CoreCommand command, Method commandMethod, Class<?> commandClass, List<SubCommandContext> subCommands) {
        this.command = command;
        this.commandMethod = commandMethod;
        try {
            this.commandClass = commandClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        this.subCommands = subCommands;
    }


    public CoreCommand getCommand() {
        return command;
    }

    public Method getCommandMethod() {
        return commandMethod;
    }

    public Object getCommandClass() {
        return commandClass;
    }

    public List<SubCommandContext> getSubCommands() {
        return subCommands;
    }


}
