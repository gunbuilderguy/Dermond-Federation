package data.hullmods.domain;

import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.combat.entities.Ship;
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


public class HITnRUNmodification extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }


    //I am in paaain :>
    public static final float speed = 1.15f;
    public static final float flox = 1.2f;
    public static final float shieldeff = 0.75f;

    public static final float shieldarc = 0.25f;
    public static final float hp = 0.75f;
    public static final float ppt = 0.5f;


/* THERE IS NONE BLOCKS!
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(16);
    static
    {
        BLOCKED_HULLMODS.add("");
    }
*/

    //Actual stats
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id, ShipAPI ship) {
        Global.getLogger(this.getClass()).info("EFFECT RUNS");
        if (stats.getVariant().hasHullMod("DermondConstruction")) {
            stats.getFluxCapacity().modifyMult(id, flox - 0.1f);
            stats.getFluxDissipation().modifyMult(id, flox - 0.1f);
            stats.getMaxSpeed().modifyMult(id, speed- 0.05f);
            stats.getTurnAcceleration().modifyMult(id, speed - 0.05f);
            stats.getShieldDamageTakenMult().modifyMult(id, shieldeff+ 0.1f);

            stats.getHullBonus().modifyMult(id, hp + 0.1f);
            stats.getArmorBonus().modifyMult(id, hp + 0.1f);
            Global.getLogger(this.getClass()).info("CONSTRUCTION");

        } if (stats.getVariant().hasHullMod("DermondReconstruction")) {
            stats.getFluxCapacity().modifyMult(id, flox - 0.15f);
            stats.getFluxDissipation().modifyMult(id, flox - 0.15f);
            stats.getMaxSpeed().modifyMult(id, speed- 0.1f);
            stats.getTurnAcceleration().modifyMult(id, speed - 0.1f);
            stats.getShieldDamageTakenMult().modifyMult(id, shieldeff+ 0.15f);

            stats.getHullBonus().modifyMult(id, hp + 0.05f);
            stats.getArmorBonus().modifyMult(id, hp + 0.05f);
            stats.getPeakCRDuration().modifyMult(id, ppt + 0.65f);
            Global.getLogger(this.getClass()).info("RECONSTRUCTION RUNS");

        } else {
            stats.getFluxCapacity().modifyMult(id, flox);
            stats.getFluxDissipation().modifyMult(id, flox);
            stats.getMaxSpeed().modifyMult(id, speed);
            stats.getTurnAcceleration().modifyMult(id, speed);
            stats.getShieldDamageTakenMult().modifyMult(id, shieldeff);

            stats.getShieldArcBonus().modifyMult(id, shieldarc);
            stats.getHullBonus().modifyMult(id, hp);
            stats.getArmorBonus().modifyMult(id, hp);
            stats.getPeakCRDuration().modifyMult(id, ppt);
            Global.getLogger(this.getClass()).info("ELSE RUNS");
        }

    }

    
    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);			
        String CSTitle = "Hit&Run Modification";
        String OrdoCrest ="graphics/icons/hullsys/interdictor_array.png" ;
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("HitnRun_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("Unknown_captain")); // Author


        tooltip.addImageWithText(PAD);
        //Also if you want to change shit also I suggest changing something here

        //Positive bonuses
        
        tooltip.addPara("%s " + getString("maxflux_increase"), pad, arr, Math.round((flox - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("fluxdis_increase"), pad, arr, Math.round((flox - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("maneuv_increase"), pad, arr, Math.round((speed - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("speed_increase"), pad, arr, Math.round((speed - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("shield_efficent"), pad, arr, Math.round((shieldeff - 1f) * -100f) + "%");    
    

        tooltip.addPara("%s " + getString("shieldarc_less"), pad, add, Math.round((shieldarc - 1f) * -100f) + "%");   
        tooltip.addPara("%s " + getString("hull_decrease"), pad, add, Math.round((hp - 1f) * -100f) + "%");   
        tooltip.addPara("%s " + getString("armour_decrease"), pad, add, Math.round((hp - 1f) * -100f) + "%");   
        tooltip.addPara("%s " + getString("ppt_less"), pad, add, Math.round((ppt - 1f) * -100f) + "%");

        tooltip.addSectionHeading("Special", Alignment.MID, pad);
            tooltip.addPara(getString("exceptions"), pad);
            tooltip.addPara(" - Dermond Constuction", pad);
            tooltip.addPara("- Dermond Reconstruction", pad);
    }

    //Bork

}