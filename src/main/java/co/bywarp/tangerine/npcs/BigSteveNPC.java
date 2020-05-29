/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.npcs;

import co.bywarp.melon.npc.Npc;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.plugin.MelonPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;

public class BigSteveNPC extends Npc<Creeper> {

    private MelonPlugin plugin;

    public BigSteveNPC(MelonPlugin plugin) {
        super(
                new Location(Bukkit.getWorld("Hub"), -78.5, 59, -93.5, 180, 180),
                Creeper.class,
                "&2&lBig Steve"
        );
        this.plugin = plugin;
    }

    @Override
    public void interact(Client client) {
    }

}
