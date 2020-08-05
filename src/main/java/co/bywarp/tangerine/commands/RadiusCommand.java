/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.commands;

import co.bywarp.lightkit.util.Ensure;
import co.bywarp.melon.command.Command;
import co.bywarp.melon.command.CommandReturn;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.Rank;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.tangerine.modules.player.ForcefieldModule;

public class RadiusCommand extends Command {

    private ForcefieldModule manager;

    public RadiusCommand(ForcefieldModule manager) {
        super(Rank.DIRECTOR, Lang.generateSingleHelp("Forcefield", "/radius <amount>"));
        this.manager = manager;
    }

    @Override
    public CommandReturn execute(Client client, String s, String[] args) {
        if (args.length != 1) {
            client.sendMessage(Lang.generate("Forcefield", "The current radius is &f" + manager.getRadius() + "&7."));
            return CommandReturn.HELP_MENU;
        }

        if (!Ensure.isNumeric(args[0])) {
            client.sendMessage(Lang.generate("Forcefield", "Invalid Radius &f[" + args[0] + "]"));
            return CommandReturn.EXIT;
        }

        manager.setRadius(Integer.parseInt(args[0]));
        client.sendMessage(Lang.generate("Forcefield", "Forcefield radius is now &f" + manager.getRadius() + "&7."));
        return CommandReturn.EXIT;
    }

}
