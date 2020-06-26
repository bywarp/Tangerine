/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.modules.rotator;

import co.bywarp.melon.player.treasure.item.TreasureItem;
import co.bywarp.melon.player.treasure.item.TreasureItemManager;
import co.bywarp.melon.player.treasure.item.TreasureItemType;
import co.bywarp.melon.plugin.MelonPlugin;
import co.bywarp.melon.util.RandomUtils;
import co.bywarp.melon.util.item.ItemBuilder;
import co.bywarp.melon.util.world.Hologram;

import co.m1ke.basic.utils.Closable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class CosmeticRotatorObject implements Closable {

    private final ItemBuilder BASE = new ItemBuilder(Material.OBSIDIAN);

    private Location location;
    private MelonPlugin plugin;
    private TreasureItemManager itemManager;
    private ArrayList<TreasureItem> items;
    private int current;

    private Hologram hologram;
    private BukkitTask rotator;
    private BukkitTask switcher;

    private ArmorStand display;
    private ArmorStand top;
    private ArmorStand bottom;

    public CosmeticRotatorObject(Location location, MelonPlugin plugin, TreasureItemManager itemManager) {
        this.location = location;
        this.hologram = new Hologram(location.clone().add(0, 0.5, 0), "&2&lTreasure")
                .add("")
                .add("&7None")
                .add("&7&oNone");
        this.plugin = plugin;
        this.itemManager = itemManager;

        World world = location.getWorld();
        this.bottom = world.spawn(location.subtract(0, 1.30, 0), ArmorStand.class);
        this.bottom.setHelmet(BASE.toItemStack());
        this.bottom.setGravity(false);
        this.bottom.setVisible(false);

        this.top = world.spawn(location.clone().add(0, .6, 0), ArmorStand.class);
        this.top.setHelmet(BASE.toItemStack());
        this.top.setGravity(false);
        this.top.setVisible(false);

        this.display = world.spawn(location.clone().add(0, 1.85, 0), ArmorStand.class);
        this.display.setSmall(true);
        this.display.setGravity(false);
        this.display.setVisible(false);

        this.items = new ArrayList<>(itemManager.getItems());

        rotate();

        this.rotator = new BukkitRunnable() {

            @Override
            public void run() {
                Location l = display.getLocation();
                l.setYaw(l.getYaw() + 9);
                display.teleport(l);
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1L);

        this.switcher = new BukkitRunnable() {
            @Override
            public void run() {
                rotate();
            }
        }.runTaskTimerAsynchronously(plugin, 200L, 200L);
    }

    public void rotate() {
        this.current = random();
        TreasureItem ci = items.get(current);

        ItemBuilder stack = new ItemBuilder(ci.material());
        if (ci.data() != 0) {
            stack.setDurability((short) ci.data());
        }

        if (ci.type() == TreasureItemType.HAT) {
            stack = new ItemBuilder(Material.SKULL_ITEM)
                    .setDurability((short) 3)
                    .setHeadValues(ci.skin());
        }

        this.display.setHelmet(stack.toItemStack());
        this.hologram.update(2, ci.rarity().getColor() + ci.name());
        this.hologram.update(3, "&7&o" + ci.category());
    }

    public int random() {
        return RandomUtils.random(0, items.size() - 1);
    }

    @Override
    public void close() {
        this.rotator.cancel();
        this.switcher.cancel();

        this.bottom.setHealth(0);
        this.bottom.remove();

        this.top.setHealth(0);
        this.bottom.remove();

        this.display.setHealth(0);
        this.display.remove();

        this.hologram.killAll();
    }
}