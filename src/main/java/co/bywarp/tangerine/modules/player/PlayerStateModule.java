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
import co.bywarp.melon.util.world.Cuboid;
import co.bywarp.tangerine.Tangerine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerStateModule extends Module {

    private Cuboid region;
    private final World HUB_WORLD = Bukkit.getWorld("Hub");

    public PlayerStateModule() {
        super("Player State");
        this.region = new Cuboid(
                new Location(HUB_WORLD, -174.5, 150, 45),
                new Location(HUB_WORLD, 123.5, 30, -238.5)
        );
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
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!region.contains(event.getTo())) {
            event.setTo(Tangerine.getGlobalSpawn());
        }
    }

}
