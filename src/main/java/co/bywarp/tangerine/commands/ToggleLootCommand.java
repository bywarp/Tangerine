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
import co.bywarp.melon.player.Rank;
import co.bywarp.melon.util.player.PlayerUtils;
import co.bywarp.melon.util.player.SoundUtil;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.tangerine.Tangerine;

import org.bukkit.Sound;

public class ToggleLootCommand extends Command {

    private Tangerine tangerine;

    public ToggleLootCommand(Tangerine tangerine) {
        super(Rank.ADMIN, Lang.generateSingleHelp("Loot", "/toggleloot"));
        this.tangerine = tangerine;
    }

    @Override
    public CommandReturn execute(Client client, String s, String[] args) {
        if (args.length != 0) {
            return CommandReturn.HELP_MENU;
        }

        tangerine.setLootDisabled(!tangerine.isLootDisabled());
        PlayerUtils.sendServerMessage(Lang.generate("Loot", "Loot is " + Lang.cond(!tangerine.isLootDisabled(), "now", "no longer") + " enabled."));
        SoundUtil.playServerSound(Sound.NOTE_PLING, 10.0f, 0.75f);

        return CommandReturn.EXIT;
    }

}
