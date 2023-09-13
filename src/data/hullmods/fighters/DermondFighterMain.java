package data.hullmods.fighters;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.DefectiveManufactory;

import java.util.HashMap;
import java.util.Map;

/*
Retardation is prime, we go monkey

 */
public class DermondFighterMain extends BaseHullMod {


    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    public static final float shit = 0.2f;

    public static Map<HullSize, Float> Hangar = new HashMap();
    static {
        Hangar.put(HullSize.FRIGATE, 1f);
        Hangar.put(HullSize.DESTROYER, 2f);
        Hangar.put(HullSize.CRUISER, 3f);
        Hangar.put(HullSize.CAPITAL_SHIP, 4f);
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //stats.getFighterRefitTimeMult().modifyPercent(id, REFIT_TIME_PLUS);
        stats.getNumFighterBays().modifyFlat(id, (Float) Hangar.get(hullSize));



    }

    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        new DefectiveManufactory().applyEffectsToFighterSpawnedByShip(fighter, ship, id);
    }


    public boolean isApplicableToShip(ShipAPI ship) {
        return true;
    }

    public String getUnapplicableReason(ShipAPI ship) {
        return null;
    }

}
