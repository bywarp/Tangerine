/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.commands;

import co.bywarp.melon.command.system.Command;
import co.bywarp.melon.command.system.CommandReturn;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.Rank;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.tangerine.modules.player.PlayerEditModule;

public class EditCommand extends Command {

    private PlayerEditModule editor;

    public EditCommand(PlayerEditModule editor) {
        super(Rank.ADMIN, Lang.generateSingleHelp("Build", "/edit"));
        this.editor = editor;
    }

    @Override
    public CommandReturn execute(Client client, String s, String[] args) {
        if (args.length != 0) {
            return CommandReturn.HELP_MENU;
        }

        if (editor.isEditor(client.getPlayer())) {
            editor.getEditors().remove(client.getId());
            client.sendMessage(Lang.generate("Build", "You are no longer an editor."));
            return CommandReturn.EXIT;
        }

        editor.getEditors().add(client.getId());
        client.sendMessage(Lang.generate("Build", "You are now an editor."));
        return CommandReturn.EXIT;
    }

}
