/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.modules.player;

import co.bywarp.melon.module.Module;
import co.bywarp.melon.parcel.prefs.ServerPreferences;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.player.Rank;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.tangerine.Tangerine;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerPostJoinModule extends Module {

    private ClientManager clientManager;
    private ServerPreferences preferences;
    private PlayerEditModule editModule;

    public PlayerPostJoinModule(PlayerEditModule editModule) {
        super("Post Player Join");
        this.editModule = editModule;
    }

    @Override
    public void start() {
        this.registerListeners();
        this.clientManager = getPlugin().getClientManager();
        this.preferences = getPlugin().getParcel().getServerPreferences();
    }

    @Override
    public void end() {
        this.unregisterListeners();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Client client = clientManager.getPlayer(event.getPlayer());
        client.teleport(Tangerine.getGlobalSpawn());

        client.getPlayer().setGameMode(GameMode.ADVENTURE);
        if (editModule.isEditor(client.getPlayer())) {
            client.getPlayer().setGameMode(GameMode.CREATIVE);
        }

        if (client.getStatisticsManager().get("prefs.donor.hubFlight").asBoolean()) {
            client.getPlayer().setAllowFlight(true);
            client.getPlayer().setFlying(true);
        }

        client.sendCenteredMessage(Lang.DIVIDER);
        client.sendMessage(" ");
        client.sendCenteredMessage("&2&lMelon Games");
        client.sendCenteredMessage("&7&oCustom Minecraft Minigames");
        client.sendMessage(" ");
        client.sendCenteredMessage("&fConnected to &a" + preferences.getSpecificServerName());
        client.sendMessage(" ");
        client.sendCenteredMessage("&aWebsite &f" + preferences.getServerWebUrl());
        client.sendCenteredMessage("&aDiscord &f" + preferences.getServerDiscordUrl());
        client.sendCenteredMessage("&aShop &f" + preferences.getServerStoreUrl());
        client.sendMessage(" ");
        client.sendCenteredMessage(Lang.DIVIDER);

        if (Rank.has(client, Rank.DIRECTOR)) {
            client.sendMessage(Lang.generate("Server", "You have been granted operator permissions."));
            client.getPlayer().setOp(true);
        }
    }

}
