/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.tangerine.recharge;

/**
 *
 * All classes that implement this become rechargeable.
 * This allows them to use the RechargeManager to implement
 * cooldown systems with ease.
 *
 */
public interface Rechargeable {

    /**
     *
     * Classes that implement this can set a default
     * name to be displayed in the recharge message.
     *
     * @return the name to be displayed in the recharge message
     */
    String getName();

    /**
     *
     * Classes that implement this can set a default
     * recharge time.
     *
     * @return the amount of recharge time
     */
    long getRechargeTime();

}