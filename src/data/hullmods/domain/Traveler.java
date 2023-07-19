package data.hullmods.domain;


import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.security.Guard;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.jcraft.jorbis.Block;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.EnergyWeaponMastery;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import java.util.HashSet;
import java.util.Set;

import data.hullmods.DermondBlockedHullmodDisplayScript;



public class Traveler extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

/*
good shit:
fuel consumption decreased by 20%
survey decreased by 5/10/15/25
salvage increased by 5/10/15/25%
cargo, fuel, crew cap by 10%
zero flux bonus by 100/50/25/15


bad shit:
supply maintenance increased by 50%
decrease burn by 2
hull decreased by 70%
armour decreased by 70%
CR per deployment increased by 200%
flux cap by 90%
flux dis by 95%
shield efficiency and phase costs by 1000%
*/

    //I don't know why would you want to make this hullmod even more OP, but here
    public static final float fuelly = -2f;
    public static final float cargo = 40f;
    public static final float supply = 0.4f;
    public static final float burn = 1f;
    public static final float hull = 0.8f;
    public static final float flox = 0.65f;
    public static final float cr = 2f;
    public static final float shield = 10f;
    public static final float trolololo = 50f;

    


    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(1);
    static
    {
        BLOCKED_HULLMODS.add("civgrade");
    }


    //Actual stats
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

        stats.getHullDamageTakenMult().modifyFlat(id, trolololo);
        stats.getPeakCRDuration().modifyMult(id, 0.1f);

        //shield stats
        stats.getShieldUpkeepMult().modifyMult(id, shield);
        stats.getShieldDamageTakenMult().modifyMult(id, shield);
        stats.getShieldMalfunctionChance().modifyMult(id, shield);

        //phase stats
        stats.getPhaseCloakActivationCostBonus().modifyMult(id, shield);
        stats.getPhaseCloakCooldownBonus().modifyMult(id, shield);
        stats.getPhaseCloakUpkeepCostBonus().modifyMult(id, shield);

        //combat stats
        stats.getFluxCapacity().modifyMult(id, flox);
        stats.getFluxDissipation().modifyMult(id, flox);
        stats.getCRLossPerSecondPercent().modifyMult(id, cr);
        stats.getHullBonus().modifyMult(id, hull);
        stats.getArmorBonus().modifyMult(id, hull);

        //logi stats
        stats.getFuelUseMod().modifyFlat(id, fuelly);
        stats.getMaxBurnLevel().modifyFlat(id, burn);
        stats.getCargoMod().modifyPercent(id, cargo);
        stats.getMaxCrewMod().modifyPercent(id, cargo);
        stats.getFuelMod().modifyPercent(id, cargo);
        stats.getSuppliesPerMonth().modifyMult(id, supply);
    }

    
    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
		String HullmodIncompatible = "graphics/icons/tooltips/der_hullmod_incompatible.png";				
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);			
        String CSTitle = "'Independent Engieneering'";
        String OrdoCrest ="graphics/factions/crest_neutral_traders.png" ;
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("independent_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("independent_guy")); // Author


        tooltip.addImageWithText(PAD);
        //Also if you want to change shit also I suggest changing something here

        tooltip.addPara("%s " + getString("maintanence_decrease"), pad, arr, Math.round((supply - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("fuel_perly_less"), pad, arr, Math.round(fuelly * -1f) + "");
        tooltip.addPara("%s " + getString("burn_more"), pad, arr, Math.round(burn) + "");
        tooltip.addPara("%s " + getString("all_cargo_more"), pad, arr, Math.round(cargo) + "%");
        

        tooltip.addPara("%s " + getString("more_shield_damage"), pad, add, Math.round((shield) * 100f) + "%");
        tooltip.addPara("%s " + getString("more_shield_flux"), pad, add, Math.round((shield) * 100f) + "%");
        tooltip.addPara("%s " + getString("phase_cost_all_more"), pad, add, Math.round((shield) * 100f) + "%");
        tooltip.addPara("%s " + getString("maxflux_less"), pad, add, Math.round((flox - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("disflux_less"), pad, add, Math.round((flox - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("degradecr_fast"), pad, add, Math.round((cr - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("hull_decrease"), pad, add, Math.round((hull - 1f ) * -100f) + "%");
        tooltip.addPara("%s " + getString("armour_decrease"), pad, add, Math.round((hull - 1f ) * -100f) + "%");
        tooltip.addPara("ERROR. ERROR. TIME AND SPACE ANOMALY DETECTED. UNKNOWN MODIFICATIONS TO SHIP DETECTED. PLEASE REFRAIN FROM USING THIS HULLMOD!", pad, add);


        tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
        TooltipMakerAPI blocked = tooltip.beginImageWithText(HullmodIncompatible, 1);
            blocked.addPara(getString("fuckyou"), pad);
            blocked.addPara("- Civilian-grade Hull", Misc.getNegativeHighlightColor(), pad);
            tooltip.addImageWithText(pad);
        }
    
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id)
    {
        for (String tmp : BLOCKED_HULLMODS)
        {
            if (ship.getVariant().getHullMods().contains(tmp))
            {
                ship.getVariant().removeMod(tmp);
                DermondBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }
}