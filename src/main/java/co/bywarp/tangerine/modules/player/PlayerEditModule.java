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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;

@Getter
public class PlayerEditModule extends Module {

    private ArrayList<UUID> editors;

    public PlayerEditModule() {
        super("Player Edit");
        this.editors = new ArrayList<>();
    }

    @Override
    public void start() {
        this.registerListeners();
    }

    @Override
    public void end() {
        this.unregisterListeners();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBuild(BlockPlaceEvent event) {
        if (editors.contains(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        if (editors.contains(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        event.setExpToDrop(0);
    }

    public boolean isEditor(Player client) {
        return editors.contains(client.getUniqueId());
    }

}
