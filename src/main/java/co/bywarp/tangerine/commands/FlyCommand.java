/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.commands;

import co.bywarp.melon.command.Command;
import co.bywarp.melon.command.CommandReturn;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.player.Rank;
import co.bywarp.melon.util.player.PlayerSearch;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.tangerine.modules.player.PlayerFlyModule;

public class FlyCommand extends Command {

    private PlayerFlyModule manager;
    private ClientManager clientManager;

    public FlyCommand(PlayerFlyModule manager, ClientManager clientManager) {
        super(Rank.PLUS, Lang.generateSingleHelp("Condition", "/fly"));
        this.manager = manager;
        this.clientManager = clientManager;
    }

    @Override
    public CommandReturn execute(Client client, String s, String[] args) {
        if (args.length == 0) {
            manager.toggle(client,
                    (res) -> client.sendMessage(Lang.generate("Condition", "You may " + Lang.cond(res, "now", "no longer") + " &7fly.")));
            return CommandReturn.EXIT;
        }

        if (Rank.has(client, Rank.MODPLUS) && args.length == 1) {
            Client target = clientManager.getByName(args[0]);
            if (target == null) {
                PlayerSearch search = new PlayerSearch(args[0]);
                client.sendMessage(search.getResults());
                return CommandReturn.EXIT;
            }

            if (client == target) {
                manager.toggle(client,
                        (res) -> client.sendMessage(Lang.generate("Condition", "&aYou &7may " + Lang.cond(res, "now", "no longer") + " &7fly.")));
                return CommandReturn.EXIT;
            }

            manager.toggle(target,
                    (res) -> {
                        client.sendMessage(Lang.generate("Condition", target.getRank().getCompatPrefix() + " &a" + target.getName() + " &7may " + Lang.cond(res, "now", "no longer") + " &7fly."));
                        target.sendMessage(Lang.generate("Condition", "You may " + Lang.cond(res, "now", "no longer") + " &7fly."));
                    });
            return CommandReturn.EXIT;
        }

        return CommandReturn.HELP_MENU;
    }

}
