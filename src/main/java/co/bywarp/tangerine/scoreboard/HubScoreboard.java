/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.scoreboard;

import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.scoreboard.ScoreboardTheme;
import co.bywarp.melon.player.scoreboard.manager.ScoreboardManager;

import java.util.ArrayList;

public abstract class HubScoreboard {

    public abstract ArrayList<String> defaultBoard(ScoreboardTheme theme);
    public abstract void onUpdate(Client client, ScoreboardManager manager);

}
