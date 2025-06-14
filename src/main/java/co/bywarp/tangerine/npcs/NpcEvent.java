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
import co.bywarp.melon.util.item.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class NpcEvent extends Npc {

    private MelonPlugin plugin;

    public NpcEvent(MelonPlugin plugin) {
        super(
                new Location(Bukkit.getWorld("Hub"), -15.5, 59.5, -73.5, 130f, 3.5f),
                EntityType.SKELETON,
                "&2&lEvents",
                " ",
                "&fThere is no current",
                "&fongoing event.",
                " ",
                "&ewww.melon.gg/events"
        );

        this.plugin = plugin;
    }

    @Override
    public void spawn() {
        super.spawn();

        Skeleton skeleton = (Skeleton) getEntity();
        skeleton.getEquipment().setItemInHand(
                new ItemBuilder(Material.SPECKLED_MELON)
                        .toItemStack()
        );
    }

    @Override
    public void interact(Client client) {
    }

}
