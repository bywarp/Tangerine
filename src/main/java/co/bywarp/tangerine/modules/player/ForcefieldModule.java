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
import co.bywarp.melon.player.Rank;
import co.bywarp.melon.util.player.PlayerUtils;
import co.bywarp.melon.util.player.SoundUtil;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;

public class ForcefieldModule extends Module {

    private BukkitTask updater;
    private ClientManager clientManager;
    @Getter @Setter private int radius;

    public ForcefieldModule(ClientManager clientManager) {
        super("Forcefield");
        this.clientManager = clientManager;
        this.radius = 5;
    }

    @Override
    public void start() {
        this.registerListeners();
        this.updater = new BukkitRunnable() {
            @Override
            public void run() {
                for (Client client : ClientManager.getOnlinePlayers()) {
                    if (!isEnabled(client)) {
                        continue;
                    }

                    for (Entity entity : client.getWorld().getNearbyEntities(client.getLocation(), radius, radius, radius)) {
                        if (entity != client.getPlayer()
                                && entity instanceof Player) {
                            Player hit = (Player) entity;
                            Client target = clientManager.getPlayer(hit);

                            if (Rank.has(target, Rank.MEDIA)) {
                                continue;
                            }

                            hit.setVelocity(hit.getLocation().getDirection().multiply(-1.25).setY(1.25));
                            SoundUtil.play(target, Sound.ENDERDRAGON_HIT);
                        }
                    }
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 20L);
    }

    @Override
    public void end() {
        this.unregisterListeners();
        if (updater != null) {
            updater.cancel();
            updater = null;
        }
    }

    private boolean isEnabled(Client client) {
        return client
                .getStatisticsManager()
                .getNodeTree()
                .get("Player")
                .getNode("Prefs")
                .getNode("Media")
                .getBoolean("Forcefield");
    }

}
