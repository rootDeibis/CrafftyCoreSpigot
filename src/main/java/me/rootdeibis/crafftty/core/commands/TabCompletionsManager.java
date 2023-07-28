package me.rootdeibis.crafftty.core.commands;

import java.util.*;

public class TabCompletionsManager {

    private final static LinkedHashSet<PlayerCompletionLimiter> delayed_completions = new LinkedHashSet<>();

    public static void limit(UUID uuid, int durationInSeconds) {
        if(delayed_completions.stream().noneMatch(d -> d.getPlayer() == uuid)) {
            delayed_completions.add(new PlayerCompletionLimiter(uuid));
        }

        delayed_completions.stream().filter(e -> e.getPlayer() == uuid).findFirst().ifPresent(d -> {
            d.setTime(durationInSeconds);
        });
    }

    public static void unregister(UUID uuid) {
        delayed_completions.removeIf(d -> d.getPlayer() == uuid);
    }

    public static void register(UUID uuid) {
        delayed_completions.add(new PlayerCompletionLimiter(uuid));
    }

    public static LinkedHashSet<PlayerCompletionLimiter> getDelayedCompletions() {
        return delayed_completions;
    }

    private static class PlayerCompletionLimiter {

        private long timestamp = new Date().getTime();
        private final UUID uuid;
        public PlayerCompletionLimiter(UUID uuid) {
            this.uuid = uuid;
        }



        public void setTime(int duration) {
            Calendar calendar = Calendar.getInstance();

            calendar.add(duration, Calendar.SECOND);

            this.timestamp = calendar.getTimeInMillis();
        }

        public boolean isAfter() {
            return new Date().after(new Date(this.timestamp));
        }

        public UUID getPlayer() {
            return uuid;
        }
    }
}
