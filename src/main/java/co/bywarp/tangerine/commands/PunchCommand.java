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
import co.bywarp.tangerine.modules.player.punch.PlayerPunchModule;

import org.bukkit.Sound;

public class PunchCommand extends Command {

    private PlayerPunchModule punchModule;

    public PunchCommand(PlayerPunchModule punchModule) {
        super(Rank.ADMIN, Lang.generateSingleHelp("Punch", "/punch"));
        this.punchModule = punchModule;
    }

    public CommandReturn execute(Client client, String cmd, String[] args) {
        if (args.length != 0) {
            return CommandReturn.HELP_MENU;
        }

        punchModule.setEnabled(!punchModule.isEnabled());
        PlayerUtils.sendServerMessage(Lang.generate("Punch", "You can " + Lang.cond(punchModule.isEnabled(), "now", "no longer") + " punch staff."));
        SoundUtil.playServerSound(Sound.NOTE_PLING, 10.0f, 0.75f);

        return CommandReturn.EXIT;
    }

}
