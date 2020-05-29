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
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.player.statistics.data.StatisticNode;
import co.bywarp.melon.util.player.SoundUtil;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerJumpModule extends Module {

    private PlayerFlyModule flyModule;

    public PlayerJumpModule(PlayerFlyModule flyModule) {
        super("Player Jump");
        this.flyModule = flyModule;
    }

    @Override
    public void start() {
        this.registerListeners();
    }

    @Override
    public void end() {
        this.unregisterListeners();
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        Client client = ClientManager.getPlayer(player.getUniqueId());
        if (!canJump(client)) {
            return;
        }

        player.setAllowFlight(true);
        player.setFlying(false);

        if (canJump(client)) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setVelocity(player.getLocation().getDirection().clone().multiply(1.5));

            StatisticNode node = client
                    .getStatisticsManager()
                    .getNodeTree()
                    .get("Player")
                    .getNode("Prefs")
                    .getNode("User");

            if (node.getBoolean("JumpSound")) {
                SoundUtil.play(client, Sound.ENDERDRAGON_WINGS, 1, 0.5f);
            }

            return;
        }

        event.setCancelled(false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Client client = ClientManager.getPlayer(player.getUniqueId());

        if (!canJump(client)) {
            return;
        }

        if (client.isFly()) {
            player.setAllowFlight(true);
            return;
        }

        if (player.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            player.setAllowFlight(true);
        }
    }

    private boolean canJump(Client client) {
        return client.getPlayer().getGameMode() != GameMode.SPECTATOR
                && client.getPlayer().getGameMode() != GameMode.CREATIVE
                && !client.getStatisticsManager().has("Donor", "HubFlight");
    }

}
