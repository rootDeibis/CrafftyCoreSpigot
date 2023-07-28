package me.rootdeibis.crafftty.core.commands.context;


import me.rootdeibis.crafftty.core.commands.annotations.CoreCommand;
import me.rootdeibis.crafftty.core.commands.annotations.TabCompletion;
import me.rootdeibis.crafftty.core.commands.MethodUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class TabCompletionContext {

    private final Method mainCompletion;
    private final Method[] subCommandCompletions;

    private final CommandContext commandContext;
    public TabCompletionContext(Method[] completionMethods, CommandContext commandContext) {
        this.mainCompletion = Arrays.stream(completionMethods).filter(m -> ((TabCompletion) m.getAnnotation(TabCompletion.class)).target() == TabCompletion.TargetType.MAIN ).findFirst().orElse(null);
        this.subCommandCompletions = Arrays.stream(completionMethods).filter(m -> ((TabCompletion) m.getAnnotation(TabCompletion.class)).target() == TabCompletion.TargetType.SUBCOMMAND ).toArray(Method[]::new);
        this.commandContext = commandContext;
    }


    public List<String> getMainCompletions(Object... arguments) {
        List<String> list = new ArrayList<>();
        try {
            Object completions = MethodUtils.invokeMethod(this.commandContext.getLoader().getCommandLoaderClass().newInstance(),  this.mainCompletion,arguments);
            return  completions instanceof List ? (List<String>) completions : list;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getSubCommandCompletions(String subCommand, Object... arguments) {
        List<String> list = new ArrayList<>();
        Predicate<Method> predicateMethod = (m -> {
            CoreCommand command = m.getAnnotation(CoreCommand.class);

            return command.name().equalsIgnoreCase(subCommand) || Arrays.stream(command.aliases()).anyMatch(a -> a.equalsIgnoreCase(subCommand));
        });

        if(Arrays.stream(this.subCommandCompletions).anyMatch(predicateMethod)) {
            Method completionMethod = Arrays.stream(this.subCommandCompletions).filter(predicateMethod).findAny().get();

            Object completions = list;
            try {
                completions = MethodUtils.invokeMethod(this.commandContext.getLoader().getCommandLoaderClass().newInstance(), completionMethod,arguments);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            return completions instanceof List ? (List<String>) completions : list;

        }

        return list;

    }
}
