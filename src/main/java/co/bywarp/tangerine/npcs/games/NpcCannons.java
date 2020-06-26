/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.npcs.games;

import co.bywarp.melon.bean.repository.ServerRepository;
import co.bywarp.melon.network.GameServerSelector;
import co.bywarp.melon.network.selector.SelectorGameType;
import co.bywarp.melon.npc.Npc;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.plugin.MelonPlugin;
import co.bywarp.melon.util.TimeUtil;
import co.bywarp.melon.util.item.ItemBuilder;
import co.bywarp.melon.util.world.Hologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class NpcCannons extends Npc {

    private MelonPlugin plugin;
    private BukkitTask updater;

    public NpcCannons(MelonPlugin plugin, ServerRepository repository) {
        super(
                new Location(Bukkit.getWorld("Hub"), -8.5, 60.5, -86.5, 115f, 3.5f),
                EntityType.SKELETON,
                "&2&lCannons",
                " ",
                "&e0 &fcurrently playing",
                "&e0 &fgame servers",
                " ",
                "&ePunch to play"
        );

        this.plugin = plugin;
        this.updater = new BukkitRunnable() {
            @Override
            public void run() {
                Hologram hologram = getHologram();
                int players = repository.getPlayers("Cannons");
                int servers = repository.getServers("Cannons").size();

                hologram.update(2, "&e" + players + " &fcurrently playing");
                hologram.update(3, "&e" + servers + " &fgame server" + TimeUtil.numberEnding(servers));
            }
        }.runTaskTimer(plugin, 0L, 20L * 5L);
    }

    @Override
    public void spawn() {
        super.spawn();

        Skeleton skeleton = (Skeleton) getEntity();
        skeleton.getEquipment().setItemInHand(
                new ItemBuilder(Material.TNT)
                        .toItemStack()
        );
    }

    @Override
    public void despawn() {
        if (updater != null) {
            updater.cancel();
        }

        super.despawn();
    }

    @Override
    public void interact(Client client) {
        GameServerSelector.serve(plugin, client, SelectorGameType.CANNONS);
    }

}
