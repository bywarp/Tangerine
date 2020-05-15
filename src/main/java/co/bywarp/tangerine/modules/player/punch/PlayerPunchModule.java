/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.modules.player.punch;

import co.bywarp.melon.module.Module;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.util.player.PlayerUtils;
import co.bywarp.melon.util.player.SoundUtil;
import co.bywarp.melon.util.text.Lang;
import co.bywarp.tangerine.commands.PunchCommand;
import co.bywarp.tangerine.recharge.RechargeManager;
import co.bywarp.tangerine.recharge.Rechargeable;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityLightning;

public class PlayerPunchModule extends Module {

    private static class PlayerPunchRecharge implements Rechargeable {
        @Override
        public String getName() {
            return "Punch Staff";
        }

        @Override
        public long getRechargeTime() {
            return 10000;
        }
    }

    private RechargeManager rechargeManager;
    @Getter @Setter private boolean enabled;

    public PlayerPunchModule(RechargeManager rechargeManager) {
        super("Staff Punch");
        this.rechargeManager = rechargeManager;
        this.enabled = true;
    }

    @Override
    public void start() {
        this.registerListeners();
        this.getPlugin().getCommandHandler()
                .registerCommand("punch", new String[] { "togglepunch" }, new PunchCommand(this));
    }

    @Override
    public void end() {
        this.unregisterListeners();
    }

    @EventHandler
    public void onPlayerPunch(PlayerPunchEvent event) {
        Client hitter = event.getPlayer();
        Client hit = event.getStaff();

        if (!enabled) {
            hitter.sendMessage(Lang.generate("Punch", "You can't do this right now."));
            return;
        }

        PlayerPunchRecharge recharge = new PlayerPunchRecharge();
        if (!rechargeManager.ensureRecharge(recharge, hitter)) {
            hitter.sendMessage(rechargeManager.getRechargeMessage(recharge, hitter));
            return;
        }

        Vector center = hitter.getLocation().toVector();
        Location player = hit.getPlayer().getLocation();
        Vector opposite = player.subtract(center).toVector();

        opposite.setY(1.0001);
//        opposite.multiply(1.0001);
        hit.getPlayer().setVelocity(opposite);

        SoundUtil.play(hit, Sound.FIREWORK_LAUNCH, 10.0f, 0.75f);
        SoundUtil.play(hitter, Sound.FIREWORK_LAUNCH, 10.0f, 0.75f);
        spawnLightning(hit.getLocation());

        PlayerUtils.sendServerMessage(Lang.generate("Punch", hitter.getRank().getCompatPrefix() + " &a" + hitter.getName() + " &7punched " + hit.getRank().getCompatPrefix() + " &a" + hit.getName() + "&7."));
    }

    private void spawnLightning(Location location) {
        net.minecraft.server.v1_8_R3.World world = ofBukkit(location.getWorld());
        EntityLightning lightning = new EntityLightning(world,
                location.getX(), location.getY(), location.getZ(),
                true, true);

        world.strikeLightning(lightning);
    }

    private net.minecraft.server.v1_8_R3.World ofBukkit(World world) {
        return ((CraftWorld) world).getHandle();
    }

}
