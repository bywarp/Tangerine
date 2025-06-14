/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.scoreboard;

import co.bywarp.melon.module.modules.defaults.staff.mfa.StaffMfaModule;
import co.bywarp.melon.module.modules.defaults.staff.mfa.StaffMfaUser;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.scoreboard.ScoreboardTheme;
import co.bywarp.melon.player.scoreboard.manager.ScoreboardManager;
import co.bywarp.melon.plugin.MelonPlugin;
import co.bywarp.melon.util.text.SimpleScoreboard;
import co.bywarp.tangerine.Tangerine;
import co.bywarp.tangerine.scoreboard.boards.MfaBoard;
import co.bywarp.tangerine.scoreboard.boards.StandardBoard;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class HubScoreboardManager extends ScoreboardManager implements Listener {

    private Client client;

    public HubScoreboardManager(Client client, ScoreboardTheme theme) {
        super(client, theme);
        this.client = super.getClient();

        Bukkit.getPluginManager().registerEvents(this, Tangerine.getPlugin(Tangerine.class));
    }

    public HubScoreboardManager(MelonPlugin plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void defaultBoard(ScoreboardTheme theme, SimpleScoreboard wrapper) {
        HubScoreboard board = getBoard(super.getClient());
        if (board == null) {
            return;
        }

        wrapper.setLines(board.defaultBoard(theme));
    }

    @Override
    public String getSuffix(Client client) {
        return "";
    }

    @Override
    public void onUpdate(Client client, ScoreboardManager manager) {
        HubScoreboard board = getBoard(client);
        if (board == null) {
            return;
        }

        board.onUpdate(client, manager);
    }

    public HubScoreboard getBoard(Client client) {
        MelonPlugin plugin = client.getPlugin();
        StaffMfaModule mfaModule = (StaffMfaModule) plugin.getModuleManager().getModule(StaffMfaModule.class);
        StaffMfaUser user = mfaModule.of(client);
        if (user.isSetup() && !user.isAuthenticated()) {
            return new MfaBoard();
        }

        return new StandardBoard();
    }

}
