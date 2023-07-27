package me.rootdeibis.crafftty;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class CrafttyLogger {

    private static final String prefix = "&eCrafttyCore &7> ";

    public static void send(String... messages) {
        Arrays.stream(messages).forEach(str -> Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + str)));
    }
}
