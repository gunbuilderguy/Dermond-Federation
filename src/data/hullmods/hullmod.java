package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class hullmod extends BaseHullMod {

    public static float Reduction = 0.9f;

    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getSensorProfile().modifyMult(id, Reduction);
        stats.getEnergyDamageTakenMult().modifyMult(id, Reduction);
        stats.getDynamic().getStat(Stats.CORONA_EFFECT_MULT).modifyMult(id, Reduction);
    }



}
