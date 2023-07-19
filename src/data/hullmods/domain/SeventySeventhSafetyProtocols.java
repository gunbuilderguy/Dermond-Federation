package data.hullmods.domain;


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





public class SeventySeventhSafetyProtocols extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

/*
+ PPT increase 40%
+ CR degradation decrease 20%
+ speed and turn 10%
*/

    //I am in paaain :>
    public static final float speed = 1.1f; 
    public static final float PPT = 1.2f;
    public static final float CR = 0.9f;


    

/* THERE IS NONE BLOCKS!
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(16);
    static
    {
        BLOCKED_HULLMODS.add("");
    }
*/

    //Actual stats
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxSpeed().modifyMult(id, speed);
        stats.getTurnAcceleration().modifyMult(id, speed);
        stats.getPeakCRDuration().modifyMult(id, PPT);
        stats.getCRLossPerSecondPercent().modifyMult(id, CR);
    }


    
    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);			
        String CSTitle = "77th Battlegroup engieneering";
        String OrdoCrest ="graphics/factions/crest_domain.png" ;
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("77th_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("77th_commander")); // Author


        tooltip.addImageWithText(PAD);
        //Also if you want to change shit also I suggest changing something here

        //Positive bonuses
        
        tooltip.addPara("%s " + getString("speed_increase"), pad, arr, Math.round((speed - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("maneuv_increase"), pad, arr, Math.round((speed - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("ppt_increase"), pad, arr, Math.round((PPT - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("degrade_less"), pad, arr, Math.round((CR - 1f) * -100f) + "%");
    
    }

    //Bork

}