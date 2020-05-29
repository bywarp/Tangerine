/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.modules.player;

import co.bywarp.melon.module.Module;
import co.bywarp.melon.network.LobbyServerSelector;
import co.bywarp.melon.network.ServerSelector;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.player.inspection.ProfileInspection;
import co.bywarp.melon.player.inspection.treasure.TreasureInspection;
import co.bywarp.melon.util.item.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHotbarModule extends Module {

    private ClientManager clientManager;
    private PlayerEditModule editor;

    public PlayerHotbarModule(ClientManager clientManager, PlayerEditModule editor) {
        super("Player Hotbar");
        this.clientManager = clientManager;
        this.editor = editor;
    }

    @Override
    public void start() {
        this.registerListeners();
    }

    @Override
    public void end() {
        this.unregisterListeners();
    }
    
    private ItemStack SERVER_SELECTOR = new ItemBuilder(Material.COMPASS)
            .setName("&aServer Selector")
            .toItemStack();

    private ItemStack LOBBY_SELECTOR = new ItemBuilder(Material.WATCH)
            .setName("&aLobby Selector")
            .toItemStack();

    private ItemStack PARTY = new ItemBuilder(Material.CHEST)
            .setName("&aParty")
            .toItemStack();

    private ItemStack HATS = new ItemBuilder(Material.STORAGE_MINECART)
            .setName("&aTreasure")
            .toItemStack();

    private ItemStack FRIENDS = new ItemBuilder(Material.BOOK)
            .setName("&aFriends")
            .toItemStack();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Client client = clientManager.getPlayer(event.getPlayer());
        Inventory inventory = client.getInventory();
        inventory.setItem(1, SERVER_SELECTOR);
        inventory.setItem(2, LOBBY_SELECTOR);
        inventory.setItem(3, PARTY);
        inventory.setItem(5, HATS);
        inventory.setItem(6, FRIENDS);
        inventory.setItem(7, getHeadItem(client));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Client client = clientManager.getPlayer(event.getPlayer());
        if (client == null) {
            return;
        }

        if (event.getAction() == Action.PHYSICAL) {
            return;
        }

        if (event.getItem() == null
                || event.getItem().getType() == Material.AIR) {
            return;
        }

        if (!event.getItem().hasItemMeta()) {
            return;
        }

        event.setCancelled(true);

        ItemStack item = event.getItem();
        if (item.isSimilar(SERVER_SELECTOR)) {
            ServerSelector.serve(getPlugin(), client);
            return;
        }

        if (item.isSimilar(LOBBY_SELECTOR)) {
            LobbyServerSelector.serve(getPlugin(), client);
            return;
        }

        if (item.isSimilar(HATS)) {
            TreasureInspection.serve(getPlugin(), client);
            return;
        }

        if (item.getType() == Material.SKULL_ITEM) {
            SkullMeta meta = (SkullMeta) event.getItem().getItemMeta();
            if (meta.getOwner().equalsIgnoreCase(client.getName())) {
                ProfileInspection profileInspection = new ProfileInspection(client, getPlugin());
                profileInspection.open();
            }
            return;
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (editor.isEditor(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if (editor.isEditor((Player) event.getWhoClicked())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if (editor.isEditor((Player) event.getInitiator().getHolder())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (editor.isEditor((Player) event.getWhoClicked())) {
            return;
        }

        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }

    private ItemStack getHeadItem(Client client) {
        return new ItemBuilder(Material.SKULL_ITEM)
                .setDurability((short) 3)
                .addHeadOptions("&aMy Profile", client.getName()).toItemStack();
    }

}
