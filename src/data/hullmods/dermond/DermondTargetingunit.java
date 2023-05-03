package data.hullmods.dermond;

//Some of these you don't need, but I will just keep them here just in case

import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.jcraft.jorbis.Block;

import data.hullmods.DermondBlockedHullmodDisplayScript;

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





public class DermondTargetingunit extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    //Positive effects
    private static Map<HullSize, Float> RANGE = new HashMap();
    static {
        RANGE.put(HullSize.FRIGATE, 30f);
        RANGE.put(HullSize.DESTROYER, 40f);
        RANGE.put(HullSize.CRUISER, 60f);
        RANGE.put(HullSize.CAPITAL_SHIP, 90f);
    }
    public static final float PD_DMG = 50f;


    //Negative effects
    private static Map<HullSize, Float> SUPPLY_USE_MULT = new HashMap();
    static {
        SUPPLY_USE_MULT.put(HullSize.FRIGATE, 30f);
        SUPPLY_USE_MULT.put(HullSize.DESTROYER, 50f);
        SUPPLY_USE_MULT.put(HullSize.CRUISER, 75f);
        SUPPLY_USE_MULT.put(HullSize.CAPITAL_SHIP, 150f);
    }
    public static final float CR_DEGRADATION = 500f;
    


    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(16);
    static
    {
        BLOCKED_HULLMODS.add("pointdefenseai");
        BLOCKED_HULLMODS.add("targetingunit");
        BLOCKED_HULLMODS.add("ballistic_rangefinder");
        BLOCKED_HULLMODS.add("advancedcore");
        BLOCKED_HULLMODS.add("apex_network_targeter");
        BLOCKED_HULLMODS.add("apex_super_range_sync");
        BLOCKED_HULLMODS.add("ass_BrokenTargeting");
        BLOCKED_HULLMODS.add("AL_glittertargetingcore");
        BLOCKED_HULLMODS.add("AL_brainAI");
        BLOCKED_HULLMODS.add("AL_boss_higgsworld");
        BLOCKED_HULLMODS.add("Knight_arrivalEnhance");
        BLOCKED_HULLMODS.add("AL_moonarcslot");
        BLOCKED_HULLMODS.add("edshipyard_pd_only_weapons");
        BLOCKED_HULLMODS.add("tahlan_centraltargeting");
        BLOCKED_HULLMODS.add("tahlan_silberherz");
        BLOCKED_HULLMODS.add("PAMED_targeting");
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //Positive

        stats.getBallisticWeaponRangeBonus().modifyPercent(id, (Float) RANGE.get(hullSize));
        stats.getEnergyWeaponRangeBonus().modifyPercent(id, (Float) RANGE.get(hullSize));
        stats.getNonBeamPDWeaponRangeBonus().modifyPercent(id, (Float) RANGE.get(hullSize));
		stats.getBeamPDWeaponRangeBonus().modifyPercent(id, (Float) RANGE.get(hullSize));

        stats.getDynamic().getMod(Stats.PD_IGNORES_FLARES).modifyFlat(id, 1f);
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 1f);
		stats.getDamageToMissiles().modifyPercent(id, PD_DMG);
        stats.getDamageToFighters().modifyPercent(id, PD_DMG);


        //Negative
        stats.getSuppliesPerMonth().modifyPercent(id, (float) SUPPLY_USE_MULT.get(hullSize));
        stats.getCRLossPerSecondPercent().modifyPercent(id, CR_DEGRADATION);
    }


    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id){

        for (String tmp : BLOCKED_HULLMODS)
        {
            if (ship.getVariant().getHullMods().contains(tmp))
            {
                ship.getVariant().removeMod(tmp);
                DermondBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    
    // Here had to be the dmg modifier but idk how to do it, so basically I can't increase PD dmg :C If somebody finds this line, and knows how to do it tell me.
    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);
		String HullmodIncompatible = "graphics/icons/tooltips/der_hullmod_incompatible.png";				
        String CSTitle = "'Post-Collapse Dermondian Engieneering'";
        String DermondCrest = "graphics/factions/crest_Dermond_Federation_messedup.png";
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI DermondIcon = tooltip.beginImageWithText(DermondCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        DermondIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        DermondIcon.addPara("%s", 6f, flavor, getString("dermond_tergetunit_desc")); //Main text
        DermondIcon.addPara("%s", 1f, flavor, getString("dermond_tergetunit_guy")); // Author


        tooltip.addImageWithText(PAD);
        //Boobies at nheantai.net :)

        //Positive bonuses
        tooltip.addPara("%s " + getString("range_increase"), pad, arr, Math.round(RANGE.get(hullSize)) + "%"  );
        tooltip.addPara("%s " + getString("increase_pddmg"), pad, arr, Math.round(PD_DMG) + "%");

        //Negative ones
        tooltip.addPara("%s " + getString("degradecr_fast"), pad, add, Math.round(CR_DEGRADATION) + "%");
        tooltip.addPara("%s " + getString("maintanence_increase"), pad, add, Math.round(SUPPLY_USE_MULT.get(hullSize)) + "%");


        
    tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
    TooltipMakerAPI blocked = tooltip.beginImageWithText(HullmodIncompatible, 40);
        blocked.addPara(getString("fuckyou"), PAD);
        blocked.addPara(" - Blocks almost any other PD or range magnification hullmod.", Misc.getNegativeHighlightColor(), pad);
        /*
        blocked.addPara("- Heavy Armor", Misc.getNegativeHighlightColor(), pad);
        if (Global.getSettings().getModManager().isModEnabled("apex_design")) {
            blocked.addPara("- Nanolaminate Plating", Misc.getNegativeHighlightColor(), pad);
            blocked.addPara("- Cryocooled Armor Lattice", Misc.getNegativeHighlightColor(), pad);
        }
        blocked.addPara("- Converted Hangar", Misc.getNegativeHighlightColor(), pad);
        if (Global.getSettings().getModManager().isModEnabled("roider")) {
            blocked.addPara("- Fighter Clamps", Misc.getNegativeHighlightColor(), pad);
        }
        if (Global.getSettings().getModManager().isModEnabled("tahlan")) {
            blocked.addPara("- Hel Plating", Misc.getNegativeHighlightColor(), pad);
        }
        if (Global.getSettings().getModManager().isModEnabled("vic")) {
            blocked.addPara(" - PD Arc Emiter", Misc.getNegativeHighlightColor(), pad);
        }
        */
        tooltip.addImageWithText(pad);
        
        if (isForModSpec || ship == null) return;
    }

    //Bork

}


