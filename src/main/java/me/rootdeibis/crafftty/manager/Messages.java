package me.rootdeibis.crafftty.manager;

import me.rootdeibis.crafftty.CrafttySpigotCore;
import org.bukkit.ChatColor;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Messages {

    public static String format(String messagePath) {
       Object message = getMessage(messagePath);

       boolean isList = message instanceof List;

       if(isList) {
           message = ChatColor.translateAlternateColorCodes('&', String.join("\n", ((List<String>) message)));
       } else {
           message = ChatColor.translateAlternateColorCodes('&', String.valueOf(message));
       }

       return (String) message;
    }

    public static String format(String messagePath, Placeholders placeholders) {
        return ChatColor.translateAlternateColorCodes('&', placeholders.apply(format(messagePath)));
    }


    private static Object getMessage(String path) {
        return CrafttySpigotCore.getFileManager().use("messages.yml").get(path);
    }

    public static class Placeholders {

        private final HashMap<String, Object> placeholders = new HashMap<>();

        public Placeholders() {
            this.placeholders.put("prefix", getMessage("prefix"));
        }


        public void add(String name, Object value) {
            this.placeholders.put(name, value);
        }

        public void remove(String name) {
            this.placeholders.remove(name);
        }

        public String apply(String target) {
            AtomicReference<String> finalStr = new AtomicReference<>(target);

            this.placeholders.forEach((key, value) -> {
                finalStr.set(finalStr.get().replaceAll("%" + key + "%", String.valueOf(value)));
            });


            return finalStr.get();

        }

    }

}
