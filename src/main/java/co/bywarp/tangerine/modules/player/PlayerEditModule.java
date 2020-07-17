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
import co.bywarp.melon.module.modules.defaults.staff.mfa.StaffMfaModule;
import co.bywarp.melon.module.modules.defaults.staff.mfa.StaffMfaUser;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.util.text.Lang;

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
    private StaffMfaModule mfaModule;

    public PlayerEditModule() {
        super("Player Edit");
        this.editors = new ArrayList<>();
    }

    @Override
    public void start() {
        this.registerListeners();
        this.mfaModule = (StaffMfaModule) getPlugin().getModuleManager().getModule(StaffMfaModule.class);
    }

    @Override
    public void end() {
        this.unregisterListeners();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBuild(BlockPlaceEvent event) {
        Client client = ClientManager.getPlayer(event.getPlayer().getUniqueId());
        if (client == null) {
            event.setCancelled(true);
            return;
        }

        boolean editor = editors.contains(client.getId());
        StaffMfaUser user = mfaModule.of(client);
        if ((user.isSetup() && !user.isAuthenticated()) && editor) {
            client.sendMessage(Lang.generate("MFA", "You must verify your identity before exacting editor privileges."));
            event.setCancelled(true);
            return;
        }

        if (editor) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        Client client = ClientManager.getPlayer(event.getPlayer().getUniqueId());
        if (client == null) {
            event.setCancelled(true);
            return;
        }

        boolean editor = editors.contains(client.getId());
        StaffMfaUser user = mfaModule.of(client);
        if ((user.isSetup() && !user.isAuthenticated()) && editor) {
            client.sendMessage(Lang.generate("MFA", "You must verify your identity before exacting editor privileges."));
            event.setCancelled(true);
            return;
        }

        if (editor) {
            return;
        }

        event.setCancelled(true);
        event.setExpToDrop(0);
    }

    public boolean isEditor(Player client) {
        return editors.contains(client.getUniqueId());
    }

}
