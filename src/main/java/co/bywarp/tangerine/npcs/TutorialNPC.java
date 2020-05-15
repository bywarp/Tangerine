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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Witch;

public class TutorialNPC extends Npc<Witch> {

    public TutorialNPC() {
        super(
                new Location(Bukkit.getWorld("Hub"), -15.5, 59.5, -87.5, 45f, 3.5f),
                Witch.class,
                "&2&lTutorials",
                " ",
                "&aTutorials &fare coming soon,",
                "&fcheck back later!"
        );
    }

    @Override
    public void spawn() {
        super.spawn();
    }

    @Override
    public void interact(Client client) {
    }

}