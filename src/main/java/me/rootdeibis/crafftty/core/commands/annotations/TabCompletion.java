package me.rootdeibis.crafftty.core.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TabCompletion {
    public static enum TargetType {
        SUBCOMMAND,
        MAIN
    }

    TargetType to() default TargetType.MAIN;

    String subcommand() default "";


}