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
import co.bywarp.melon.network.ServerSelector;
import co.bywarp.melon.network.selector.SelectorGameType;
import co.bywarp.melon.network.selector.SelectorServerType;
import co.bywarp.melon.npc.Npc;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.plugin.MelonPlugin;
import co.bywarp.melon.util.TimeUtil;
import co.bywarp.melon.util.inventory.handlers.SelectionInventory;
import co.bywarp.melon.util.item.ItemBuilder;
import co.bywarp.melon.util.world.Hologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.NumberFormat;
import java.util.Locale;

public class NpcDeathmatch extends Npc {

    private MelonPlugin plugin;
    private BukkitTask updater;
    private ServerRepository repository;
    private NumberFormat nf;

    private final SelectorServerType TYPE = SelectorServerType.DEATHMATCH;

    public NpcDeathmatch(MelonPlugin plugin, ServerRepository repository) {
        super(
                new Location(Bukkit.getWorld("Hub"), -16.5, 60.5, -66.5, 155f, 3.5f),
                EntityType.SKELETON,
                "&2&lDeathmatch",
                " ",
                "&e0 &fcurrently playing",
                "&e0 &fgame servers",
                " ",
                "&ePunch to play"
        );

        this.plugin = plugin;
        this.repository = repository;
        this.nf = NumberFormat.getInstance(Locale.US);
        this.updater = new BukkitRunnable() {
            @Override
            public void run() {
                Hologram hologram = getHologram();
                int players = repository.getPlayers(TYPE.getName());
                int servers = repository.getAmountOfServers(TYPE.getName());

                for (SelectorGameType variant : TYPE.getVariants()) {
                    players += repository.getPlayers(variant.getNameLong());
                    servers += repository.getAmountOfServers(variant.getNameLong());
                }

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
                new ItemBuilder(Material.BLAZE_POWDER)
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
        new SelectionInventory<>(plugin, client,
                handler -> GameServerSelector.serve(plugin, client, handler.getResponse()),
                gameType -> {
                    int players = repository.getPlayers(gameType.getNameLong());
                    int servers = repository.getAmountOfServers(gameType.getNameLong());
                    return gameType
                            .getItem()
                            .clone()
                            .setName("&2&l" + gameType.getNameLong())
                            .setLore("&e" + nf.format(players) + " &fcurrently playing\n" +
                                    "&e" + nf.format(servers) + " &fserver" + TimeUtil.numberEnding(servers) + "\n\n" +
                                    "&aClick to view " + gameType.getNameLong() + " servers");
                },
                () -> ServerSelector.serve(plugin, client),
                "Select a Variant",
                TYPE.getVariants().toArray(new SelectorGameType[0]));
    }

}
