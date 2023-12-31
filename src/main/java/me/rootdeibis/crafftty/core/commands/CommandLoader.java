package me.rootdeibis.crafftty.core.commands;

import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.TabCompletion;
import me.rootdeibis.crafftty.core.commands.context.CommandContext;
import me.rootdeibis.crafftty.core.commands.executors.CoreCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandLoader {
    private final Class<?> commandloaderClass;
    private final Object initializedClass;



    public CommandLoader(Class<?> commandLoaderClass) {
        this.commandloaderClass = commandLoaderClass;
        try {
            this.initializedClass = commandLoaderClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Method[] getCommandMethods() {
        return Arrays.stream(this.commandloaderClass.getMethods()).filter(m -> m.isAnnotationPresent(CoreCommand.class)).toArray(Method[]::new);
    }

    public Method[] getTabCompletionMethods() {
        return Arrays.stream(this.commandloaderClass.getMethods()).filter(m -> m.isAnnotationPresent(TabCompletion.class)).toArray(Method[]::new);
    }

    public Class<?> getCommandLoaderClass() {
        return commandloaderClass;
    }

    public void register() {
        for (Method commandMethod : this.getCommandMethods()) {
            CommandContext commandContext = new CommandContext(this, commandMethod);

            this.getCommandMap().register(commandContext.getCommand().name(), new CoreCommandExecutor(commandContext));

        }
    }

    public Object getInitializedClass() {
        return initializedClass;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = null;

        try {
            Field f = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }

        return commandMap;
    }

}
