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





public class DermondBogorta extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    //Positive effects

    public static Map<HullSize, Float> PPT_increase = new HashMap();
    static {
        PPT_increase.put(HullSize.FRIGATE, 500f);
        PPT_increase.put(HullSize.DESTROYER, 40f);
        PPT_increase.put(HullSize.CRUISER, 20f);
        PPT_increase.put(HullSize.CAPITAL_SHIP, 10f);
    }

    public static Map<HullSize, Float> ENERGY_DAMAGE = new HashMap();
    static {
        ENERGY_DAMAGE.put(HullSize.FRIGATE, 60f);
        ENERGY_DAMAGE.put(HullSize.DESTROYER, 40f);
        ENERGY_DAMAGE.put(HullSize.CRUISER, 25f);
        ENERGY_DAMAGE.put(HullSize.CAPITAL_SHIP, 15f);
    }

    //I am in paaain :>
    public static final float Shpeed = 0.6f; //FUCK YOU!
    public static final float turny_bitty_poopy = 0.6f; //Poo poo
    public static final float COWARD = 1.9f;
    public static final float hippy_hoppity_your_flux_is_now_my_property = 2f;
    public static final float flox = 0.9f;


    

/* THERE IS NONE BLOCKS!
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(16);
    static
    {
        BLOCKED_HULLMODS.add("");
    }
*/

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //OP shiet
        stats.getPeakCRDuration().modifyPercent(id, (Float) PPT_increase.get(hullSize));
        stats.getEnergyWeaponDamageMult().modifyPercent(id, (Float) ENERGY_DAMAGE.get(hullSize));

        //Meeeh I want ship op! If you are this kind of person just remove this shit you filthy casual!
        stats.getMaxSpeed().modifyMult(id, Shpeed);
        stats.getTurnAcceleration().modifyMult(id, turny_bitty_poopy);
        stats.getMaxTurnRate().modifyMult(id, turny_bitty_poopy);
        stats.getShieldDamageTakenMult().modifyMult(id, COWARD);
        stats.getPhaseCloakActivationCostBonus().modifyMult(id, hippy_hoppity_your_flux_is_now_my_property);
        stats.getPhaseCloakCooldownBonus().modifyMult(id, hippy_hoppity_your_flux_is_now_my_property);
        stats.getPhaseCloakUpkeepCostBonus().modifyMult(id,hippy_hoppity_your_flux_is_now_my_property);
        stats.getFluxCapacity().modifyMult(id, flox);
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
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("testament5_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("testament5_guy")); // Author


        tooltip.addImageWithText(PAD);
        //Also if you want to change shit also I suggest changing something here

        //Positive bonuses
        tooltip.addPara("%s " + getString("increase_ppt"), pad, arr, Math.round(PPT_increase.get(hullSize)) + "%");
        tooltip.addPara("%s " + getString("energy_increase"), pad, arr, Math.round(ENERGY_DAMAGE.get(hullSize)) + "%");

        //Poo poo I just shat my pants I am weak so I must remove this so people can't see I am weak WAAAAAAAH!
        //And yes I am bored now go and shitpost to me in DMs!
        tooltip.addPara("%s " + getString("less_speed"), pad, add, Math.round(Shpeed * 100f) + "%");
        tooltip.addPara("%s " + getString("less_turn"), pad, add, Math.round(turny_bitty_poopy * 100f) + "%");
        tooltip.addPara("%s " + getString("more_shield_damage"), pad, add, Math.round((COWARD- 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("phase_cost_all_more"), pad, add, Math.round((hippy_hoppity_your_flux_is_now_my_property - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("maxflux_less"), pad, add, Math.round((flox - 1f) * -100f) + "%");

    
    }

    //Bork

}


