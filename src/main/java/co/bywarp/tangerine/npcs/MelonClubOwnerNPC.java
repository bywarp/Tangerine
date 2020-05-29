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

import co.m1ke.basic.utils.RandomUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;

import java.util.ArrayList;

public class MelonClubOwnerNPC extends Npc<Skeleton> {

    private ArrayList<String> phrases = new ArrayList<String>() {
        {
            add(Lang.generate("&a&lMelon Club Owner", "Melon Club is currently closed. Go away."));
            add(Lang.generate("&a&lMelon Club Owner", "I haven't had time to get Melon Club running. It's closed."));
            add(Lang.generate("&a&lMelon Club Owner", "Rebuilding Melon Club is going to cost me a fortune."));
            add(Lang.generate("&a&lMelon Club Owner", "Sorry, we're closed."));
            add(Lang.generate("&a&lMelon Club Owner", "Man, I really wish I had about 1.5K more coins!"));
            add(Lang.generate("&a&lMelon Club Owner", "I hate &d&lKaexe&7, everytime I do something good, he destroys it."));
            add(Lang.generate("&a&lMelon Club Owner", "Melon Club is currently closed. Go away."));
        }
    };

    public MelonClubOwnerNPC() {
        super(
                new Location(Bukkit.getWorld("Hub"), -54.5, 57.5, -34.5),
                Skeleton.class,
                "&2&lMelon Club Owner"
        );
    }

    @Override
    public void spawn() {
        super.spawn();

        Skeleton skeleton = (Skeleton) getEntity();
        skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER);
        skeleton.getEquipment().setItemInHand(new ItemBuilder(Material.MELON_BLOCK)
                .toItemStack());
    }

    @Override
    public void interact(Client client) {
        String random = phrases.get(RandomUtils.getRandomInt(0, phrases.size()));
        if (random == null) {
            return;
        }

        client.sendMessage(random);
    }

}
