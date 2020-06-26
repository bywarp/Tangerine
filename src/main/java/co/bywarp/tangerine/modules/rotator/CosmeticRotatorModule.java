/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.modules.rotator;

import co.bywarp.melon.module.Module;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CosmeticRotatorModule extends Module {

    private CosmeticRotatorObject object;

    public CosmeticRotatorModule() {
        super("Cosmetic Rotator");
    }

    @Override
    public void start() {
        this.object = new CosmeticRotatorObject(new Location(Bukkit.getWorld("Hub"), -29.5, 59, -73.5),
                getPlugin(), getPlugin().getTreasureItemManager());
    }

    @Override
    public void end() {
        if (this.object != null) {
            this.object.close();
            this.object = null;
        }
    }

}
