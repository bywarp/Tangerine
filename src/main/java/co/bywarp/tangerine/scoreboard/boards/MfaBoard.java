/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.scoreboard.boards;

import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.scoreboard.ScoreboardTheme;
import co.bywarp.melon.player.scoreboard.manager.ScoreboardManager;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.tangerine.scoreboard.HubScoreboard;

import java.util.ArrayList;

public class MfaBoard extends HubScoreboard {

    @Override
    public ArrayList<String> defaultBoard(ScoreboardTheme theme) {
        String divider = theme.getDivider().toString();

        ArrayList<String> li = new ArrayList<>();
        li.add(Lang.colorMessage("&r" + divider + "&m-----------------------"));
        li.add(Lang.colorMessage("&fverification code."));
        li.add(Lang.colorMessage("&fin order to input your"));
        li.add(Lang.colorMessage("&fUse &a/auth login <code>"));
        li.add(Lang.colorMessage("&e&r" + divider + "&m----------------------"));
        li.add(Lang.colorMessage("&fto interact with the network."));
        li.add(Lang.colorMessage("&fyour identity in order"));
        li.add(Lang.colorMessage("&f&cStop! &fYou must verify"));
        li.add(Lang.colorMessage("&2&lAuthentication"));
        li.add(Lang.colorMessage("&d&r" + divider + "&m---------------------- "));

        return li;
    }

    @Override
    public void onUpdate(Client client, ScoreboardManager manager) {
    }

}
