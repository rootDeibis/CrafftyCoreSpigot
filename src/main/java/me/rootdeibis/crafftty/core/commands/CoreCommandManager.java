package me.rootdeibis.crafftty.core.commands;

import me.rootdeibis.crafftty.CrafttySpigotCore;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.CoreCommandLoader;
import me.rootdeibis.crafftty.core.commands.annotations.CoreSubCommands;
import me.rootdeibis.crafftty.manager.Files.RFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
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
import java.util.HashMap;
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
            CoreSubCommands subCommands = method.getAnnotation(CoreSubCommands.class);


            List<SubCommandContext> subcommands = new ArrayList<>();


            if(subCommands != null) {

                for (Class<?> sClass : subCommands.list()) {
                    Arrays.stream(sClass.getMethods()).filter(m -> m.isAnnotationPresent(CoreCommand.class)).findFirst().ifPresent(sMethod -> subcommands.add(new SubCommandContext(method.getAnnotation(CoreCommand.class),sClass, sMethod)));

                }

            }


            CommandContext commandContext = new CommandContext(command, method, clazz);

            CommandContextExecutor contextExecutor = new CommandContextExecutor(commandContext);

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

}
