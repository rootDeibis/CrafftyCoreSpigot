package me.rootdeibis.crafftty.core.commands;

import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class SubCommandContext {

    private final CoreCommand subcommand;

    private final Object subcommandClass;

    private final Method subcommandMethod;

    public SubCommandContext(CoreCommand command, Class<?> sClass, Method sMethod) {
        this.subcommand = command;
        try {
            this.subcommandClass = sClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.subcommandMethod = sMethod;
    }

    public CoreCommand getSubcommand() {
        return subcommand;
    }

    public Method getSubcommandMethod() {
        return subcommandMethod;
    }

    public Object getSubcommandClass() {
        return subcommandClass;
    }
}
