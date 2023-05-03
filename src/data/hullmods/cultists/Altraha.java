package data.hullmods.cultists;


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





public class Altraha extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }


    public static final float ammocap = 0.75f;
    public static final float ballisticspeed = 0.7f;

    public static final float beamdmg = 1.2f;
    public static final float beamrange = 1.3f;


    

/* THERE IS NONE BLOCKS!
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(16);
    static
    {
        BLOCKED_HULLMODS.add("");
    }
*/
    //Actual stats
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getBallisticAmmoBonus().modifyMult(id, ammocap);
        stats.getBallisticProjectileSpeedMult().modifyMult(id, ballisticspeed);

        
        stats.getBeamWeaponDamageMult().modifyMult(id, beamdmg);
        stats.getBeamWeaponRangeBonus().modifyMult(id, beamrange);
        stats.getBeamPDWeaponRangeBonus().modifyMult(id, beamrange);
    }


    
    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);			
        String CSTitle = "'Ordo Aeterni' 2nd Generation Engieneering";
        String OrdoCrest ="graphics/factions/crest_dermond_Cultists.png" ;
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("cult_godlasers_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("dopro_guy")); // Author


        tooltip.addImageWithText(PAD);
        //Also if you want to change shit also I suggest changing something here

        tooltip.addPara("%s " + getString("beamdamage_more"), pad, arr, Math.round((beamdmg - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("beamrange_more"), pad, arr, Math.round((beamrange - 1f) * 100f) + "%");

        
        tooltip.addPara("%s " + getString("ballisticammo_less"), pad, add, Math.round((ammocap - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("ballisticprojectiles_less"), pad, add, Math.round((ballisticspeed - 1f) * -100f) + "%");
    
    }

    //Bork

}