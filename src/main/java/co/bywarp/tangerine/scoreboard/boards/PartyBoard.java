/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.scoreboard.boards;

import co.bywarp.melon.parcel.prefs.ServerPreferences;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.player.Rank;
import co.bywarp.melon.player.scoreboard.ScoreboardTheme;
import co.bywarp.melon.player.scoreboard.manager.ScoreboardManager;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.tangerine.Tangerine;
import co.bywarp.tangerine.scoreboard.HubScoreboard;

import org.bukkit.Bukkit;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PartyBoard extends HubScoreboard {

    @Override
    public ArrayList<String> defaultBoard(ScoreboardTheme theme) {
        String leads = theme.getLeads().toString();
        String sec = theme.getSecondary().toString();
        String divider = theme.getDivider().toString();

        ServerPreferences serverPreferences = Tangerine.getServerPreferences();
        String serverName = serverPreferences.getSpecificServerName().replaceAll("-", " #");

        ArrayList<String> li = new ArrayList<>();
        li.add(Lang.colorMessage("&r" + divider + "&m-----------------------"));
        li.add(Lang.colorMessage(String.format("%sWebsite: %s%s", leads, sec, "www.mci.gg")));
        li.add(Lang.colorMessage(String.format("%sPlayers: %s%s/%s", leads, sec, ClientManager.getOnlinePlayers().size(), Bukkit.getMaxPlayers())));
        li.add(Lang.colorMessage("&d"));
        li.add(Lang.colorMessage("&fCoins: &aLoading.."));
        li.add(Lang.colorMessage(String.format("%sServer: %s%s", leads, sec, serverName)));
        li.add(Lang.colorMessage(String.format("%sRank: Loading..", leads)));
        li.add(Lang.colorMessage("&e&r" + divider + "&m----------------------"));
        li.add(Lang.colorMessage("&fMembers: &aLoading.."));
        li.add(Lang.colorMessage("&fLeader: &aLoading.."));
        li.add(Lang.colorMessage("&a&lParty"));
        li.add(Lang.colorMessage("&d&r" + divider + "&m---------------------- "));

        return li;
    }

    @Override
    public void onUpdate(Client client, ScoreboardManager manager) {
        ScoreboardTheme theme = manager.getTheme();
        String leads = theme.getLeads().toString();
        String sec = theme.getSecondary().toString();

        manager.set(2, leads + "Players: " + sec + ClientManager.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
        manager.set(4, leads + "Coins: " + sec + NumberFormat.getInstance(Locale.US).format(client.getCoins()));
        manager.set(6, leads + "Rank: " + Lang.cond(client.getRank() == Rank.MEMBER, "&7Member", client.getRank().getCompatPrefix()));
        manager.set(8, leads + "Members: " + sec + "0");
        manager.set(9, leads + "Leader: " + sec + "None");
    }

}
