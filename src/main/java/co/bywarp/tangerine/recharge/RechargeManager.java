/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.recharge;

import co.bywarp.lightkit.util.Closable;
import co.bywarp.melon.player.Client;
import co.bywarp.melon.player.ClientManager;
import co.bywarp.melon.plugin.MelonPlugin;
import co.bywarp.melon.util.TimeUtil;
import co.bywarp.melon.util.player.PlayerUtils;
import co.bywarp.melon.util.player.SoundUtil;
import co.bywarp.melon.util.text.Lang;

import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

@Getter
public class RechargeManager implements Closable {

    private Map<String, Map<UUID, Long>> recharging;
    private ArrayList<Rechargeable> rechargeables;
    private MelonPlugin plugin;
    private ClientManager clientManager;
    private BukkitTask alerts;
    private BukkitTask actionBars;

    /**
     *
     * Manage rechargable items in the hub
     * @param plugin the melon
     *
     */
    public RechargeManager(MelonPlugin plugin) {
        this.plugin = plugin;
        this.clientManager = plugin.getClientManager();
        this.recharging = new HashMap<>();
        this.rechargeables = new ArrayList<>();

        this.alerts = new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<UUID> remove = new ArrayList<>();
                for (Map.Entry<String, Map<UUID, Long>> ent : recharging.entrySet()) {
                    for (UUID recharging : ent.getValue().keySet()) {
                        Client client = ClientManager.getPlayer(recharging);
                        if (client == null) {
                            continue;
                        }

                        long timeLeft = ent.getValue().getOrDefault(recharging, 0L) - System.currentTimeMillis();
                        if (timeLeft <= 0) {
                            remove.add(client.getId());
                            String full = Lang.colorMessage("&fYou can now use &a" + ent.getKey());
                            PlayerUtils.sendActionBar(client, full);
                            SoundUtil.play(client, Sound.NOTE_PLING, 10.0f, 0.6f);
                            client.sendMessage(Lang.generate("Recharge", "You can now use &f" + ent.getKey()) + "&7.");
                        }
                    }
                    for (UUID id : remove) {
                        ent.getValue().remove(id);
                    }
                    remove.clear();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);

        this.actionBars = new BukkitRunnable() {
            @Override
            public void run() {
                for (java.util.Map.Entry<String, java.util.Map<UUID, Long>> ent : getRecharging().entrySet()) {
                    String rechargeable = ent.getKey();
                    java.util.Map<UUID, Long> data = ent.getValue();

                    data.forEach((uuid, end) -> {
                        Client client = clientManager.getByUuid(uuid);
                        if (client == null) {
                            return;
                        }

                        long timeLeft = end - System.currentTimeMillis();
                        if (timeLeft < 0) {
                            return;
                        }

                        Rechargeable recharge = getByName(rechargeable);
                        if (recharge == null) {
                            return;
                        }

                        double dec = timeLeft / 1000.0;
                        String timeFormat = TimeUtil.getDecimalFormat().format(dec);
                        timeFormat = Integer.toString((int) dec).contains(".") ? dec + ".0" : timeFormat;

                        String full = Lang.colorMessage("&a" + rechargeable + " &fwill recharge in &a" + timeFormat + "s"/* &7&l⏐ &a" + o + "&f" + oo*/);
                        PlayerUtils.sendActionBar(client, full);
                    });

                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     *
     * Set an ability to cooling down for a certain player
     *
     * @param recharge the ability
     * @param client the player who needs to cooldown
     */
    public void recharge(Rechargeable recharge, Client client) {
        String name = recharge.getName();

        if (!rechargeables.contains(recharge)) {
            rechargeables.add(recharge);
        }

        Map<UUID, Long> map = recharging.getOrDefault(name, new HashMap<>());
        map.put(client.getId(), System.currentTimeMillis() + recharge.getRechargeTime());
        recharging.put(name, map);
    }

    /**
     *
     * Check if a player is recharging for a certain ability
     *
     * @param recharge the ability
     * @param client the client who needs checked
     * @return if the player is recharging or not
     */
    public boolean isRecharging(Rechargeable recharge, Client client) {
        Map<UUID, Long> map = recharging.getOrDefault(recharge.getName(), new HashMap<>());
        UUID uuid = client.getId();

        if (!map.containsKey(uuid)) {
            return false;
        }

        long timeLeft = map.getOrDefault(uuid, 0L) - System.currentTimeMillis();
        return timeLeft >= 0;
    }

    /**
     * Gets the recharge message for a rechargeable.
     *
     * @param rechargeable the rechargeable
     * @param client the client
     * @return the message to send the client
     */
    public String getRechargeMessage(Rechargeable rechargeable, Client client) {
        Map<UUID, Long> map = getRecharging().get(rechargeable.getName());
        UUID uuid = client.getId();
        if (map.isEmpty() || map.getOrDefault(client.getId(), 0L) <= 0) {
            return "";
        }

        long timeLeft = map.getOrDefault(uuid, 0L) - System.currentTimeMillis();
        return Lang.generate("Recharge", "You cannot use &f" + rechargeable.getName() + " &7for another &f" + TimeUtil.getShortenedTimeValue(timeLeft) + "&7.");
    }

    /**
     * Removes a recharge for a client if applicable.
     * @param rechargeable the rechargable
     * @param client the client
     */
    public void removeRecharge(Rechargeable rechargeable, Client client) {
        if (isRecharging(rechargeable, client)) {
            recharging.get(rechargeable.getName()).remove(client.getId());
        }
    }

    /**
     * Removes all recharges for a client.
     * @param client the client
     */
    public void removeAllRecharge(Client client) {
        for (Map<UUID, Long> ent : recharging.values()) {
            if (ent.get(client.getId()) != null) {
                ent.remove(client.getId());
            }
        }
    }

    public Rechargeable getByName(String name) {
        return rechargeables
                .stream()
                .filter(rechargeable -> rechargeable.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean ensureRecharge(Rechargeable rechargeable, Client client) {
        if (isRecharging(rechargeable, client)) {
            return false;
        }

        recharge(rechargeable, client);
        return true;
    }

    @Override
    public void close() {
        if (alerts != null) {
            alerts.cancel();
        }

        if (actionBars != null) {
            actionBars.cancel();
        }

        if (recharging != null) {
            recharging.clear();
        }
    }

}