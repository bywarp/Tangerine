/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.npcs;

import co.bywarp.melon.npc.Npc;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.util.item.ItemBuilder;
import co.bywarp.melon.util.text.Lang;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.PigZombie;

public class TutorialNPC extends Npc<PigZombie> {

    public TutorialNPC() {
        super(
                new Location(Bukkit.getWorld("Hub"), -15.5, 59, -87.5, -90f, 3.5f),
                PigZombie.class,
                "&2&lTutorials",
                " ",
                "&aTutorials &fare coming soon,",
                "&fcheck back later!"
        );
    }

    @Override
    public void spawn() {
        super.spawn();

        PigZombie zombie = (PigZombie) getEntity();
        zombie.setAngry(false);
        zombie.getEquipment().setItemInHand(
                new ItemBuilder(Material.DIAMOND_HOE)
                .addEnchantment(Enchantment.DIG_SPEED, 10)
                .toItemStack()
        );
        zombie.getEquipment().setLeggings(new ItemBuilder(Material.GOLD_LEGGINGS).toItemStack());
    }

    @Override
    public void interact(Client client) {
        client.sendMessage(Lang.generate("Melon", "&aTutorials &7are coming soon."));
    }

}