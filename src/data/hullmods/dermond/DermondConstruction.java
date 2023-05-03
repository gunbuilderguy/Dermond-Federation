package data.hullmods.dermond;

//Some of these you don't need, but I will just keep them here just in case

import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import data.hullmods.DermondBlockedHullmodDisplayScript;

import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashSet;
import java.util.Set;

public class DermondConstruction extends BaseHullMod {

    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }
        


    //Positive Bonuses
	public static final float HULL_BONUS = 10f;
	public static final float HEALTH_BONUS = 25f;
    public static final float CORONA_EFFECT_REDUCTION = 0.85f;
	public static final float ENERGY_DAMAGE_REDUCTION = 0.9f;
    public static final float HE_REDUCTION = 0.95f;
    public static final float ARMOR_DAMAGE_REDUCTION = 0.95f;
    public static final float PPT_MULT = 1.5f;



    //Negative Bonuses
    public static final float SUPPLY_USE_MULT = 3f;
    public static final float DEGRADE_INCREASE_PERCENT = 60f;
    public static final float PROFILE_MULT = 1.25f;



    //Blocked
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(5);

    static
    {
        // These hullmods will automatically be removed
        // Not as elegant as blocking them in the first place, but
        // this method doesn't require editing every hullmod's script
        BLOCKED_HULLMODS.add("tahlan_daemonplating");
        BLOCKED_HULLMODS.add("apex_armor");
        BLOCKED_HULLMODS.add("apex_cryo_armor");
        BLOCKED_HULLMODS.add("converted_hangar");
        BLOCKED_HULLMODS.add("roider_fighterClamps");
    }


    //damage reduction
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

        //Positive Bonuses
        stats.getHighExplosiveDamageTakenMult().modifyMult(id, 1f - HE_REDUCTION / 100f);
        stats.getArmorDamageTakenMult().modifyMult(id, 1f - ARMOR_DAMAGE_REDUCTION / 100f);
        stats.getPeakCRDuration().modifyMult(id, PPT_MULT);
        stats.getEngineHealthBonus().modifyPercent(id, HEALTH_BONUS);
        stats.getHullBonus().modifyPercent(id, HULL_BONUS);
        stats.getEnergyDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_REDUCTION);
        stats.getEnergyShieldDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_REDUCTION);
        //stats.getBeamDamageTakenMult().modifyMult(id, BEAM_DAMAGE_REDUCTION);
        stats.getDynamic().getStat(Stats.CORONA_EFFECT_MULT).modifyMult(id, CORONA_EFFECT_REDUCTION);


        //Negative Bonuses
        stats.getSensorProfile().modifyMult(id, PROFILE_MULT);
        stats.getSuppliesPerMonth().modifyMult(id, SUPPLY_USE_MULT);
        stats.getCRLossPerSecondPercent().modifyPercent(id, DEGRADE_INCREASE_PERCENT);

           

        }

    public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && (ship.getHullSpec().getNoCRLossTime() < 10000 || ship.getHullSpec().getCRLossPerSecond() > 0); //this was in Hardened subsystems or effcency hullmod? I don't remember.
	}

    @Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return !isForModSpec;
    }


	@Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);
		String HullmodIncompatible = "graphics/icons/tooltips/der_hullmod_incompatible.png";				
        String CSTitle = "'Dermondian Engieneering'";
        String DermondCrest = "graphics/factions/crest_Dermond_Federation.png";
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI DermondIcon = tooltip.beginImageWithText(DermondCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        DermondIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        DermondIcon.addPara("%s", 6f, flavor, getString("construction_desc")); //Main text
        DermondIcon.addPara("%s", 1f, flavor, getString("Ornium")); // Author


        tooltip.addImageWithText(PAD);


        //Positive bonuses
        tooltip.addPara("%s " + getString("hull_bonus"), pad, arr, Math.round(HULL_BONUS) + "%"  );
        tooltip.addPara("%s " + getString("corona_reduction"), pad, arr, Math.round((1f - CORONA_EFFECT_REDUCTION) * 100f) + "%");
        tooltip.addPara("%s " + getString("energy_reduction"), pad, arr, Math.round((1f - ENERGY_DAMAGE_REDUCTION) * 100f) + "%");
        tooltip.addPara("%s " + getString("armordmg_reduction"), pad, arr, Math.round((1f - ARMOR_DAMAGE_REDUCTION) * 100f) + "%");
        tooltip.addPara("%s " + getString("engine_increase"), pad, arr, Math.round(HEALTH_BONUS) + "%");
        tooltip.addPara("%s " + getString("ppt_increase"), pad, arr, Math.round((PPT_MULT - 1f) * 100) + "%");

        //Negative ones
        tooltip.addPara("%s " + getString("profile_increase"), pad, add, Math.round(((PROFILE_MULT - 1f) * 100f)) + "%");
        tooltip.addPara("%s " + getString("degradecr_fast"), pad, add, Math.round(DEGRADE_INCREASE_PERCENT) + "%");
        tooltip.addPara("%s " + getString("maintanence_increase"), pad, add, Math.round(((SUPPLY_USE_MULT - 1f) * 100f)) + "%");


        
	    tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
        TooltipMakerAPI blocked = tooltip.beginImageWithText(HullmodIncompatible, 40);
            blocked.addPara(getString("fuckyou"), PAD);
            
            if (Global.getSettings().getModManager().isModEnabled("tahlan")) {
                blocked.addPara("- Hel Plating", Misc.getNegativeHighlightColor(), PAD);
            }
            if (Global.getSettings().getModManager().isModEnabled("apex_design")) {
                blocked.addPara("- Nanolaminate Plating", Misc.getNegativeHighlightColor(), PAD);
                blocked.addPara("- Cryocooled Armor Lattice", Misc.getNegativeHighlightColor(), PAD);
            }
            blocked.addPara("- Converted Hangar", Misc.getNegativeHighlightColor(),PAD);
            if (Global.getSettings().getModManager().isModEnabled("roider")) {
                blocked.addPara("- Fighter Clamps", Misc.getNegativeHighlightColor(), PAD);
            }
    
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