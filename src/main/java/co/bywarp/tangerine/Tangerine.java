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
import co.bywarp.melon.module.modules.treasure.PlayerTreasureModule;
import co.bywarp.melon.module.modules.treasure.TreasureChestModule;
import co.bywarp.melon.network.NetworkPlayerCount;
import co.bywarp.melon.npc.NpcManager;
import co.bywarp.melon.npc.global.NpcDailyReward;
import co.bywarp.melon.player.treasure.chest.TreasureChest;
import co.bywarp.melon.plugin.MelonPlugin;
import co.bywarp.tangerine.commands.EditCommand;
import co.bywarp.tangerine.commands.RadiusCommand;
import co.bywarp.tangerine.commands.ToggleLootCommand;
import co.bywarp.tangerine.modules.AntiWeatherModule;
import co.bywarp.tangerine.modules.player.ForcefieldModule;
import co.bywarp.tangerine.modules.player.PlayerEditModule;
import co.bywarp.tangerine.modules.player.PlayerFlyModule;
import co.bywarp.tangerine.modules.player.PlayerHotbarModule;
import co.bywarp.tangerine.modules.player.PlayerJumpModule;
import co.bywarp.tangerine.modules.player.PlayerPostJoinModule;
import co.bywarp.tangerine.modules.player.PlayerStackerModule;
import co.bywarp.tangerine.modules.player.PlayerStateModule;
import co.bywarp.tangerine.modules.player.punch.PlayerPunchModule;
import co.bywarp.tangerine.modules.rotator.CosmeticRotatorModule;
import co.bywarp.tangerine.npcs.NpcEvent;
import co.bywarp.tangerine.npcs.NpcMelonClubOwner;
import co.bywarp.tangerine.npcs.NpcTutorial;
import co.bywarp.tangerine.npcs.games.NpcBingo;
import co.bywarp.tangerine.npcs.games.NpcCannons;
import co.bywarp.tangerine.npcs.games.NpcDeathmatch;
import co.bywarp.tangerine.npcs.games.NpcMicroArcade;
import co.bywarp.tangerine.recharge.RechargeManager;
import co.bywarp.tangerine.scoreboard.HubScoreboardManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import lombok.Getter;
import lombok.Setter;

public class Tangerine extends MelonPlugin {

    @Getter private static Location globalSpawn;
    @Getter private static NetworkPlayerCount networkPlayerCount;
    @Getter private static RechargeManager rechargeManager;
    @Getter private static VanishModule vanishModule;
    @Getter private static ChatStatusModule chatStatus;

    @Getter @Setter private boolean lootDisabled;

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
        rechargeManager.close();
    }

    @EventHandler
    @Override
    public void onInit(MelonPluginInitializationEvent event) {
        super.onInit(event);

        globalSpawn = new Location(Bukkit.getWorld("Hub"), -22.5, 60, -80.5, -90, 0);
        networkPlayerCount = getPlayerCount();
        rechargeManager = new RechargeManager(this);
        vanishModule = (VanishModule) getModuleManager().getModule(VanishModule.class);
        chatStatus = (ChatStatusModule) getModuleManager().getModule(ChatStatusModule.class);
        lootDisabled = false;

        ServerRepository serverRepository = this.getBeans().getServerRepository();
        NpcManager npc = this.getNpcManager();

        npc.registerNPC(new NpcDailyReward(this, new Location(Bukkit.getWorld("Hub"), -6.5, 59.75, -80.5, 90f, 3.5f)));
        npc.registerNPC(new NpcEvent(this));
        npc.registerNPC(new NpcBingo(this, serverRepository));
        npc.registerNPC(new NpcDeathmatch(this, serverRepository));
        npc.registerNPC(new NpcCannons(this, serverRepository));
        npc.registerNPC(new NpcMicroArcade(this, serverRepository));
        npc.registerNPC(new NpcTutorial());
        npc.registerNPC(new NpcMelonClubOwner());

        ModuleManager modules = this.getModuleManager();

        PlayerEditModule editModule;
        PlayerFlyModule flyModule;
        ForcefieldModule forcefieldModule;

        PlayerTreasureModule<Tangerine> treasureModule = new PlayerTreasureModule<>(this, Tangerine::isLootDisabled);

        modules.load(new AntiWeatherModule());
        modules.load(new CosmeticRotatorModule());
        modules.load(editModule = new PlayerEditModule());
        modules.load(new PlayerHotbarModule(this.getClientManager(), editModule));
        modules.load(treasureModule);
        modules.load(flyModule = new PlayerFlyModule());
        modules.load(new PlayerJumpModule(flyModule));
        modules.load(new PlayerPostJoinModule(editModule));
        modules.load(new PlayerStackerModule(this.getClientManager()));
        modules.load(new PlayerStateModule());
        modules.load(new PlayerPunchModule(rechargeManager));
        modules.load(forcefieldModule = new ForcefieldModule(this.getClientManager()));

        TreasureChest chest = new TreasureChest(
                this,
                this.getTreasureItemManager(),
                new Location(getGlobalSpawn().getWorld(), -30, 59, -88));
        TreasureChestModule treasureChestModule = new TreasureChestModule(chest);
        modules.load(treasureChestModule);

        CommandHandler commandHandler = this.getCommandHandler();
        commandHandler.registerCommand("edit", new String[] { "build" }, new EditCommand(editModule, getClientManager()));
        commandHandler.registerCommand("radius", new RadiusCommand(forcefieldModule));
        commandHandler.registerCommand("toggleloot", new ToggleLootCommand(this));

        this.setScoreboardManager(new HubScoreboardManager(this));

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.getPluginManager().callEvent(new PlayerJoinEvent(player, null));
        }
    }

}
