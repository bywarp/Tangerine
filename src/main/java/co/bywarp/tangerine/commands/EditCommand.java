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
import co.bywarp.tangerine.modules.player.PlayerEditModule;
import co.bywarp.tangerine.modules.player.PlayerHotbarModule;

import org.bukkit.GameMode;

public class EditCommand extends Command {

    private PlayerEditModule editor;
    private ClientManager clientManager;

    public EditCommand(PlayerEditModule editor, ClientManager clientManager) {
        super(Rank.ADMIN, Lang.generateSingleHelp("Editor", "/edit <client>"));
        this.editor = editor;
        this.clientManager = clientManager;
    }

    @Override
    public CommandReturn execute(Client client, String s, String[] args) {
        if (args.length != 0 && args.length != 1) {
            return CommandReturn.HELP_MENU;
        }

        if (args.length == 0) {
            execute(client, client);
            return CommandReturn.EXIT;
        }

        Client target = clientManager.getByName(args[0]);
        if (target == null) {
            PlayerSearch search = new PlayerSearch(args[0]);
            client.sendMessage(search.getResults());
            return CommandReturn.EXIT;
        }

        execute(target, client);
        return CommandReturn.EXIT;
    }

    private void execute(Client client, Client staff) {
        if (editor.isEditor(client.getPlayer())) {
            editor.getEditors().remove(client.getId());

            if (client != staff) {
                staff.sendMessage(Lang.generate("Editor", client.getRank().getCompatPrefix() + " &a" + client.getName() + " &7no longer has edit privileges."));
            }

            client.sendMessage(Lang.generate("Editor", "You no longer have editor privileges."));
            client.getInventory().clear();
            client.getPlayer().setGameMode(GameMode.ADVENTURE);
            PlayerHotbarModule.applyHotbar(client);
            return;
        }

        editor.getEditors().add(client.getId());

        if (client != staff) {
            staff.sendMessage(Lang.generate("Editor", client.getRank().getCompatPrefix() + " &a" + client.getName() + " &7now has edit privileges."));
        }

        client.sendMessage(Lang.generate("Editor", "You now have editor privileges."));
        client.getPlayer().setGameMode(GameMode.CREATIVE);
        client.getInventory().clear();
    }

}
