package me.rootdeibis.crafftty.core.commands;

import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommandLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoreCommandManager {


    public static void scan(String commandsPackage) {

        Reflections reflections = new Reflections(commandsPackage, new SubTypesScanner(false));

        reflections.getAllTypes().forEach(clazz -> {

            try {

                Class<?> commandClass = Class.forName(clazz);

                CoreCommandLoader loader = commandClass.getAnnotation(CoreCommandLoader.class);

                if(loader == null) return;

                Method[] methods = commandClass.getMethods();

                List<Method> commands_methods = new ArrayList<>();

                for (Method method : methods) {

                    CoreCommand command = method.getAnnotation(CoreCommand.class);


                    if(command != null) {
                        commands_methods.add(method);
                    }
                    
                }

                registerCommands(commands_methods, commandClass);






            }catch (Exception e) {
                e.printStackTrace();
            }

        });

    }


    private static void registerCommands(List<Method> methods, Class<?> clazz) {
        methods.forEach(method -> {

            CoreCommand command = method.getAnnotation(CoreCommand.class);

            CommandContextExecutor contextExecutor = new CommandContextExecutor(command.name(), command, method, clazz);

            contextExecutor.setAliases(Arrays.stream(command.aliases()).collect(Collectors.toList()));
            contextExecutor.setPermission(command.permission());

            getCommandMap().register(command.name(), contextExecutor);

        });
    }

    private static CommandMap getCommandMap() {
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

    private static class CommandContextExecutor extends BukkitCommand {

        private final CoreCommand commandMethod;
        private final Method method;
        private final Object declaredClass;
        protected CommandContextExecutor(String name, CoreCommand commandMethod, Method method,Class<?> clazz) {
            super(name);
            this.commandMethod = commandMethod;
            this.method = method;

            try {
                this.declaredClass = clazz.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean execute(CommandSender commandSender, String label, String[] strings) {

            try {
                this.method.invoke(this.declaredClass, commandSender, label, strings);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            return false;
        }
    }
}
