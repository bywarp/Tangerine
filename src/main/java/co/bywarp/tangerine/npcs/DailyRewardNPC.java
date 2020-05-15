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
import co.bywarp.melon.util.text.Lang;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class DailyRewardNPC extends Npc<Villager> implements Listener {

    private MelonPlugin plugin;

    public DailyRewardNPC(MelonPlugin plugin) {
        super(
                new Location(Bukkit.getWorld("Hub"), -6.5, 59, -80.5, 90f, 3.5f),
                Villager.class,
                "&2&lThe Delivery Boy",
                " ",
                "&aRewards &fare coming soon,",
                "&fcheck back later!"
        );
        this.plugin = plugin;
        this.plugin.registerEvents(this);
    }

    @Override
    public void spawn() {
        super.spawn();

        Villager villager = (Villager)getEntity();
        villager.setProfession(Villager.Profession.LIBRARIAN);
        villager.setAdult();
    }

    @Override
    public void interact(Client client) {
    }

    @Override
    public void despawn() {
        HandlerList.unregisterAll(this);
        super.despawn();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.MERCHANT) {
            event.setCancelled(true);
        }
    }

}
