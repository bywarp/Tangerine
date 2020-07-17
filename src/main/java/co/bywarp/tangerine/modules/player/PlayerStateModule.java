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
import co.bywarp.melon.module.ModuleManager;
import co.bywarp.melon.module.modules.defaults.staff.mfa.StaffMfaModule;
import co.bywarp.melon.module.modules.defaults.staff.mfa.StaffMfaUser;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.player.Rank;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.melon.util.world.Cuboid;
import co.bywarp.tangerine.Tangerine;
import co.bywarp.tangerine.modules.player.punch.PlayerPunchEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerStateModule extends Module {

    private Cuboid region;
    private ClientManager clientManager;
    private StaffMfaModule mfaModule;
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
        this.clientManager = getPlugin().getClientManager();

        ModuleManager manager = getPlugin().getModuleManager();
        this.mfaModule = (StaffMfaModule) manager.getModule(StaffMfaModule.class);
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPunch(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)
                || !(event.getEntity() instanceof Player)) {
            return;
        }

        Client hit = clientManager.getPlayer((Player) event.getEntity());
        if (!Rank.isStaff(hit)) {
            return;
        }

        Client hitter = clientManager.getPlayer((Player) event.getDamager());
        if (!Rank.isDonor(hitter)) {
            return;
        }

        Bukkit.getPluginManager().callEvent(new PlayerPunchEvent(hit, hitter));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/auth")) {
           return;
        }

        if (mfaModule == null) {
            return;
        }

        Client client = clientManager.getPlayer(event.getPlayer());
        if (client == null) {
            return;
        }

        StaffMfaUser user = mfaModule.of(client);
        if (user.isSetup() && !user.isAuthenticated()) {
            client.sendMessage(Lang.generate("MFA", "You must verify your identity before performing commands."));
            event.setCancelled(true);
            return;
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
