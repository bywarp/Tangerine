/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.modules.player;

import co.bywarp.melon.command.CommandHandler;
import co.bywarp.melon.module.Module;
import co.bywarp.melon.player.Client;
import co.bywarp.tangerine.commands.FlyCommand;

import java.util.function.Consumer;

public class PlayerFlyModule extends Module {

    public PlayerFlyModule() {
        super("Player Fly");
    }

    @Override
    public void start() {
        CommandHandler manager = this.getPlugin().getCommandHandler();
        manager.unregisterCommand("fly");
        manager.registerCommand("fly", new FlyCommand(this, getPlugin().getClientManager()));
    }

    @Override
    public void end() {
    }

    public void toggle(Client client, Consumer<Boolean> then) {
        boolean state = client.getStatisticsManager().has("Donor", "HubFlight");
        client.getPlayer().setAllowFlight(!state);
        client.getPlayer().setFlying(!state);

        client.getStatisticsManager().toggle("Donor", "HubFlight");
        then.accept(!state);
    }

    public boolean has(Client client) {
        return client.getStatisticsManager().has("Donor", "HubFlight");
    }

}
