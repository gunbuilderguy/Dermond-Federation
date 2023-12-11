package data.hullmods.cultists;

//Some of these you don't need, but I will just keep them here just in case

import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
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


public class Hardella extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    //Positive effects
    //public static final Float RANGE = 60f;
    public static Map<HullSize, Float> PPT_increase = new HashMap();
    static {
        PPT_increase.put(HullSize.FRIGATE, 30f);
        PPT_increase.put(HullSize.DESTROYER, 60f);
        PPT_increase.put(HullSize.CRUISER, 150f);
        PPT_increase.put(HullSize.CAPITAL_SHIP, 240f);
    }
    public static final float SUPPLY_USE_MULT = 0.8f;

    //Negative effects
    //Wait there is none?
    

/* THERE IS NONE BLOCKS!
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
*/
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //Positive

        //stats.getBallisticWeaponRangeBonus().modifyPercent(id, (Float) RANGE);
        //stats.getEnergyWeaponRangeBonus().modifyPercent(id, (Float) RANGE);

        stats.getPeakCRDuration().modifyFlat(id, (Float) PPT_increase.get(hullSize));

        //Negative
        stats.getSuppliesPerMonth().modifyMult(id, SUPPLY_USE_MULT);

    }


    
    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);			
        String CSTitle = "'Ordo Aeterni' 3rd Generation Engieneering";
        String OrdoCrest ="graphics/factions/crest_dermond_Cultists.png" ;
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};	
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("testament5_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("testament5_guy")); // Author


        tooltip.addImageWithText(PAD);
        //Boobies at nheantai.net :)

        //Positive bonuses
        //tooltip.addPara("%s " + getString("range_increase"), pad, arr, Math.round(RANGE) + "%"  );
        tooltip.addPara("%s " + getString("increase_ppt"), pad, arr, Math.round(PPT_increase.get(hullSize)) + " seconds ");
        tooltip.addPara("%s " + getString("maintanence_decrease"), pad, arr, Math.round(((SUPPLY_USE_MULT - 1f) * -100f)) + "%");

        if (isForModSpec || ship == null) return;
    }

    //Bork

}


