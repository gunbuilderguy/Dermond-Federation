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





public class Kullastro extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    //I am in paaain :>
    public static final float ability_regen = 0.9f;
    public static final float reload = 0.9f;
    public static final float armour = 0.95f;
    public static final float ppt = 0.8f;

    public static final float flox = 1.35f;
    public static final float sped = 1.1f;
    public static final float emp = 0.9f;


    

/* THERE IS NONE BLOCKS!
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(16);
    static
    {
        BLOCKED_HULLMODS.add("");
    }
*/

    //Actual stats
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getSystemRegenBonus().modifyMult(id, ability_regen);
        stats.getBallisticAmmoRegenMult().modifyMult(id, reload);
        stats.getArmorBonus().modifyMult(id, armour);
        stats.getPeakCRDuration().modifyMult(id, ppt);

        stats.getFluxCapacity().modifyMult(id, flox);
        stats.getFluxDissipation().modifyMult(id, flox);
        stats.getMaxSpeed().modifyMult(id, sped);
        stats.getTurnAcceleration().modifyMult(id, sped);
        stats.getEmpDamageTakenMult().modifyMult(id, emp);
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
        OrdoIcon.addPara("%s", 6f, flavor, getString("kullastro_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("dopro_guy")); // Author


        tooltip.addImageWithText(PAD);
        //Also if you want to change shit also I suggest changing something here

        tooltip.addPara("%s " + getString("maxflux_increase"), pad, arr, Math.round((flox - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("fluxdis_increase"), pad, arr, Math.round((flox - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("speed_increase"), pad, arr, Math.round((sped - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("maneuv_increase"), pad, arr, Math.round((sped - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("emp_less"), pad, arr, Math.round((emp - 1f) * -100f) + "%");

        
        tooltip.addPara("%s " + getString("systemregen_increase"), pad, add, Math.round((ability_regen - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("ballisticreload_less"), pad, add, Math.round((reload - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("armour_decrease"), pad, add, Math.round((armour - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("ppt_less"), pad, add, Math.round((ppt - 1f) * -100f) + "%");

    }

    //Bork

}