/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine;

import co.bywarp.melon.bean.repository.ServerRepository;
import co.bywarp.melon.command.CommandHandler;
import co.bywarp.melon.event.MelonPluginInitializationEvent;
import co.bywarp.melon.module.ModuleManager;
import co.bywarp.melon.module.defaults.DefaultModules;
import co.bywarp.melon.module.modules.defaults.player.chat.ChatStatusModule;
import co.bywarp.melon.module.modules.defaults.staff.vanish.VanishModule;
import co.bywarp.melon.network.NetworkPlayerCount;
import co.bywarp.melon.npc.NpcManager;
import co.bywarp.melon.plugin.MelonPlugin;
import co.bywarp.tangerine.commands.EditCommand;
import co.bywarp.tangerine.commands.RadiusCommand;
import co.bywarp.tangerine.modules.AntiWeatherModule;
import co.bywarp.tangerine.modules.player.ForcefieldModule;
import co.bywarp.tangerine.modules.player.PlayerEditModule;
import co.bywarp.tangerine.modules.player.PlayerHotbarModule;
import co.bywarp.tangerine.modules.player.PlayerJumpModule;
import co.bywarp.tangerine.modules.player.PlayerPostJoinModule;
import co.bywarp.tangerine.modules.player.PlayerStackerModule;
import co.bywarp.tangerine.modules.player.PlayerStateModule;
import co.bywarp.tangerine.npcs.DailyRewardNPC;
import co.bywarp.tangerine.npcs.EventNPC;
import co.bywarp.tangerine.npcs.games.BingoNPC;
import co.bywarp.tangerine.npcs.games.CannonsNPC;
import co.bywarp.tangerine.npcs.games.DeathRunNPC;
import co.bywarp.tangerine.npcs.games.InfectedNPC;
import co.bywarp.tangerine.npcs.TutorialNPC;
import co.bywarp.tangerine.npcs.games.MicroArcadeNPC;
import co.bywarp.tangerine.scoreboard.HubScoreboardManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import lombok.Getter;

public class Tangerine extends MelonPlugin {

    @Getter private static Location globalSpawn;
    @Getter private static NetworkPlayerCount networkPlayerCount;
    @Getter private static VanishModule vanishModule;
    @Getter private static ChatStatusModule chatStatus;

    public Tangerine() {
        super("Tangerine", 0.1, DefaultModules.allOn());
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    @Override
    public void onInit(MelonPluginInitializationEvent event) {
        super.onInit(event);

        globalSpawn = new Location(Bukkit.getWorld("Hub"), -22.5, 60, -80.5, -90, 0);
        networkPlayerCount = getPlayerCount();
        vanishModule = (VanishModule) getModuleManager().getModule(VanishModule.class);
        chatStatus = (ChatStatusModule) getModuleManager().getModule(ChatStatusModule.class);

        ServerRepository serverRepository = this.getBeans().getServerRepository();
        NpcManager npc = this.getNpcManager();

        npc.registerNPC(new DailyRewardNPC(this));
        npc.registerNPC(new EventNPC(this));
        npc.registerNPC(new BingoNPC(this, serverRepository));
        npc.registerNPC(new DeathRunNPC(this, serverRepository));
        npc.registerNPC(new InfectedNPC(this, serverRepository));
        npc.registerNPC(new CannonsNPC(this, serverRepository));
        npc.registerNPC(new MicroArcadeNPC(this, serverRepository));
        npc.registerNPC(new TutorialNPC());

        ModuleManager modules = this.getModuleManager();
        PlayerEditModule editModule;
        ForcefieldModule forcefieldModule;

        modules.load(new AntiWeatherModule());
        modules.load(editModule = new PlayerEditModule());
        modules.load(new PlayerHotbarModule(this.getClientManager(), editModule));
        modules.load(new PlayerJumpModule());
        modules.load(new PlayerPostJoinModule());
        modules.load(new PlayerStackerModule(this.getClientManager()));
        modules.load(new PlayerStateModule());
        modules.load(forcefieldModule = new ForcefieldModule(this.getClientManager()));

        CommandHandler command = this.getCommandHandler();
        command.registerCommand("edit", new String[] { "build" }, new EditCommand(editModule));
        command.registerCommand("radius", new RadiusCommand(forcefieldModule));

        this.setScoreboardManager(new HubScoreboardManager(this));

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.getPluginManager().callEvent(new PlayerJoinEvent(player, null));
        }
    }

}
