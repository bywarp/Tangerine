/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.npcs;

import co.bywarp.lightkit.util.RandomUtils;
import co.bywarp.melon.npc.Npc;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.util.item.ItemBuilder;
import co.bywarp.melon.util.text.Lang;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

import java.util.ArrayList;

public class NpcMelonClubOwner extends Npc {

    // you were never here
    private final String OOPS = "From here to the stars,\n" +
            "With my candy bars,\n" +
            "Rides a kid\n" +
            "With a knack\n" +
            "For inventions.\n" +
            "\n" +
            "A super-powered mind,\n" +
            "A mechanical canine,\n" +
            "He rescues the day\n" +
            "From sure destruction.\n" +
            "\n" +
            "He's gotta save the world\n" +
            "And get to school on time,\n" +
            "So many things to do\n" +
            "And not much timeSo off the ground,\n" +
            "Up in the air,\n" +
            "Out into the atmoshpere,\n" +
            "\n" +
            "Who can we count on?\n" +
            "Jimmy Neutron!\n" +
            "Who can we count on?\n" +
            "Jimmy Neutron!\n" +
            "Who can we count on?\n" +
            "Jimmy Neutron!\n" +
            "\n" +
            "No matter where you are,\n" +
            "You know he can't be far,\n" +
            "Watching the world\n" +
            "Through x-ray vision\n" +
            "\n" +
            "And whenever he's around,\n" +
            "Where adventure can be found,\n" +
            "You know that Jimmy Neutron's\n" +
            "On a mission\n" +
            "\n" +
            "He's gotta save the world\n" +
            "And get to school on time,\n" +
            "So many things to do\n" +
            "And not much time\n" +
            "\n" +
            "So off the ground,\n" +
            "Up in the air,\n" +
            "Out into the atmoshpere,\n" +
            "\n" +
            "Who can we count on?\n" +
            "Jimmy Neutron!\n";

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

    public NpcMelonClubOwner() {
        super(
                new Location(Bukkit.getWorld("Hub"), -54.5, 57.5, -34.5),
                EntityType.SKELETON,
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
        String random = RandomUtils.random(phrases);
        if (random == null) {
            client.sendMessage(Lang.colorMessage(OOPS));
            return;
        }

        client.sendMessage(random);
    }

}
