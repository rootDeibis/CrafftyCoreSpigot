package me.rootdeibis.crafftty.auth.hook;

import com.nickuc.login.api.nLoginAPI;
import com.github.games647.fastlogin.core.hooks.AuthPlugin;
import com.nickuc.login.api.types.Identity;
import org.bukkit.entity.Player;

public class FastNLoginAuth implements AuthPlugin<Player> {

    private final nLoginAPI api;
    public FastNLoginAuth() {
        this.api = nLoginAPI.getApi();


    }
    @Override
    public boolean forceLogin(Player player) {
        return this.api.forceLogin(this.getIdentity(player.getName()));
    }

    @Override
    public boolean forceRegister(Player player, String s) {

        return this.api.performRegister(this.getIdentity(player.getName()), s);
    }

    @Override
    public boolean isRegistered(String s) throws Exception {
        return this.api.getAccount(this.getIdentity(s)).orElse(null) != null;
    }

    private Identity getIdentity(String name) {
        return  this.api.internal().createIdentityFromKnownName(name);
    }
}
