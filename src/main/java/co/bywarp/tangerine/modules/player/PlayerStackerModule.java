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
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.util.text.Lang;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerStackerModule extends Module {

    private ClientManager clientManager;

    public PlayerStackerModule(ClientManager clientManager) {
        super("Stacker");
        this.clientManager = clientManager;
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
    public void onStack(PlayerInteractAtEntityEvent event) {
        Client clicker = clientManager.getPlayer(event.getPlayer());
        Entity clicked = event.getRightClicked();
        if (clicked instanceof Player) {
            Client clickedClient = clientManager.getPlayer((Player) clicked);

            if (!isEnabled(clicker)) {
                clicker.sendMessage(Lang.generate("Stacker", "You don't have stacker enabled."));
                return;
            }

            if (!isEnabled(clickedClient)) {
                clicker.sendMessage(Lang.generate("Stacker", clickedClient.getRank().getCompatPrefix() + " &a" + clickedClient.getName() + " &7doesn't have stacker enabled."));
                return;
            }

            if (isEnabled(clickedClient) && isEnabled(clicker)) {
                Entity passenger = clicker.getPlayer();
                while (passenger.getPassenger() != null) {
                    passenger = passenger.getPassenger();
                }

                clicker.sendMessage(Lang.generate("Stacker", "Picked up " + clickedClient.getRank().getCompatPrefix() + " &a" + clickedClient.getName() + "&7."));
                clickedClient.sendMessage(Lang.generate("Stacker", clicker.getRank().getCompatPrefix() + " &a" + clicker.getName() + " &7picked you up."));

                if (passenger == clickedClient.getPlayer()) {
                    return;
                }

                passenger.setPassenger(clickedClient.getPlayer());
            }
        }
    }

    @EventHandler
    public void onUnstack(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.PHYSICAL) {
            Client client = clientManager.getPlayer(event.getPlayer());
            if (client.getPlayer().getPassenger() != null) {
                Entity toThrow = client.getPlayer().getPassenger();
                Vector vec = client.getPlayer().getLocation().getDirection().clone().multiply(2).setY(2);
                if (toThrow.getPassenger() == null) {
                    toThrow.leaveVehicle();
                    toThrow.setVelocity(vec);
                    return;
                }

                while (toThrow.getPassenger() != null) {
                    toThrow.leaveVehicle();
                    toThrow.setVelocity(vec);
                    toThrow = toThrow.getPassenger();
                }
            }
        }
    }

    private boolean isEnabled(Client client) {
        return client.getStatisticsManager()
                .getNodeTree()
                .get("Player")
                .getNode("Prefs")
                .getNode("User")
                .getBoolean("Stacker");
    }

}
