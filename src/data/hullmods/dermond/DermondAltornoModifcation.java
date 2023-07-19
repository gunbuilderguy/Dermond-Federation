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
import com.fs.starfarer.api.impl.campaign.rulecmd.AddStoryPoints;
import com.fs.starfarer.api.impl.campaign.skills.EnergyWeaponMastery;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import java.util.HashSet;
import java.util.Set;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ArmorGridAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;




public class DermondAltornoModifcation extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    //Positive effects
    public static final float fuckdamage = 0.4f;

    //Negative effects
    public static final float flux = 0.3f;
    public static final float SUPPLY_USE_MULT = 1.75f;
    public static final float FUEL_TRAVEL = 3f;
    public static final float shpeed = 0.25f;

    int hmp_CA_T = 0;
    private static final Color OUTLINE_COLOR = new Color(0f, 0.15f, 0.8f, 0.027f);
    private static final Color OVERLAY_COLOR = new Color(0.05f, 0.00f, 0.3f, 0.006f);

    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(35);
    static
    {
        BLOCKED_HULLMODS.add("armoredweapons");
        BLOCKED_HULLMODS.add("auxiliarythrusters");
        BLOCKED_HULLMODS.add("eccm");
        BLOCKED_HULLMODS.add("ecm");
        BLOCKED_HULLMODS.add("extendedshieldemitter");
        BLOCKED_HULLMODS.add("fluxcoil");
        BLOCKED_HULLMODS.add("fluxdistributor");
        BLOCKED_HULLMODS.add("frontemitter");
        BLOCKED_HULLMODS.add("frontshield");
        BLOCKED_HULLMODS.add("shield_shunt");
        BLOCKED_HULLMODS.add("hardened_subsystems");
        BLOCKED_HULLMODS.add("adaptiveshields");
        BLOCKED_HULLMODS.add("fluxbreakers");
        BLOCKED_HULLMODS.add("reinforcedhull");
        BLOCKED_HULLMODS.add("stabilizedshieldemitter");
        BLOCKED_HULLMODS.add("unstable_injector");
        BLOCKED_HULLMODS.add("insulatedengine");
        BLOCKED_HULLMODS.add("solar_shielding");
        BLOCKED_HULLMODS.add("hmp_regenerativearmor");
        BLOCKED_HULLMODS.add("hmp_crystalizedarmor");
        BLOCKED_HULLMODS.add("hmp_enhancedstructure");
        BLOCKED_HULLMODS.add("hmp_traxymiumarmor");
        BLOCKED_HULLMODS.add("hmp_fluxcoreultradrive");
        BLOCKED_HULLMODS.add("mhmods_integratedarmor");
        BLOCKED_HULLMODS.add("ocua_shield_module");
        BLOCKED_HULLMODS.add("uaf_auroranEngineering");
        BLOCKED_HULLMODS.add("uaf_shield_manipulator");
        BLOCKED_HULLMODS.add("vic_deathProtocol");
        BLOCKED_HULLMODS.add("tahlan_daemonarmor");
        BLOCKED_HULLMODS.add("tahlan_daemonplating");
        BLOCKED_HULLMODS.add("tahlan_helhoundarmor");
        BLOCKED_HULLMODS.add("tahlan_helaltar");
        BLOCKED_HULLMODS.add("tahlan_chmlegio");
        BLOCKED_HULLMODS.add("tahlan_lirefit");
        BLOCKED_HULLMODS.add("tahlan_knightrefit");
    }


    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //Positive
        stats.getHullDamageTakenMult().modifyMult(id, fuckdamage);
        stats.getArmorDamageTakenMult().modifyMult(id, fuckdamage);
        stats.getEnergyDamageTakenMult().modifyMult(id, fuckdamage);
        stats.getHighExplosiveDamageTakenMult().modifyMult(id, fuckdamage);
        stats.getEnergyDamageTakenMult().modifyMult(id, fuckdamage);


        //Negative
        stats.getSuppliesPerMonth().modifyMult(id, SUPPLY_USE_MULT);
        stats.getFluxCapacity().modifyMult(id, flux);
        stats.getFluxDissipation().modifyMult(id, flux);
        stats.getFuelUseMod().modifyFlat(id, FUEL_TRAVEL);
        stats.getMaxSpeed().modifyMult(id, shpeed);
        stats.getTurnAcceleration().modifyMult(id, shpeed);
        stats.getMaxTurnRate().modifyMult(id, shpeed);

    }

    @Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ship.setShield(ShieldType.NONE, 0f, 1f, 1f);
        for (String tmp : BLOCKED_HULLMODS)
        {
            if (ship.getVariant().getHullMods().contains(tmp))
            {
                ship.getVariant().removeMod(tmp);
                DermondBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
	}

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) return;
        MutableShipStatsAPI stats = ship.getMutableStats();
        Vector2f initialOffset = MathUtils.getRandomPointInCircle(null, 20f);
        Vector2f specificOffset = MathUtils.getRandomPointInCircle(initialOffset, 0f);
        ship.addAfterimage(
                OUTLINE_COLOR,
                specificOffset.x,
                specificOffset.y,
                0f,     // Horizontal speed modifier.
                0f,     // Vertical speed modifier.
                0f,    // jitter (Distortion).
                0.3f,   //Enter duration.
                1f,     //Stay duration.
                0.3f,   //Exit duration.
                true,   //Additive blend.
                true,   //Combine with sprite color.
                false   //Above ship.
        );
        initialOffset = MathUtils.getRandomPointInCircle(null, 0f);
        specificOffset = MathUtils.getRandomPointInCircle(initialOffset, 0f);
        ship.addAfterimage(
                OVERLAY_COLOR,
                specificOffset.x,
                specificOffset.y,
                0f, // Horizontal speed modifier.
                0f, // Vertical speed modifier.
                0f, // jitter (Distortion).
                0.3f, //Enter duration.
                1f,   //Stay duration.
                0.3f, //Exit duration.
                true, //Additive blend.
                true, //Combine with sprite color.
                true  //Above ship.
        );
    }

    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);	
        String HullmodIncompatible = "graphics/icons/tooltips/der_hullmod_incompatible.png";		
        String CSTitle = "'Post-Collapse Dermondian Engieneering'";
        String OrdoCrest = "graphics/factions/crest_Dermond_Federation_messedup.png";
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("damper_but_better")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("manfred_buffett")); // Author


        tooltip.addImageWithText(PAD);
        //Boobies at nheantai.net :)

        //Positive bonuses
        tooltip.addPara("%s" + getString("GOD"), pad, arr, Math.round((fuckdamage -1f) * -100f) + "%");


        tooltip.addPara("%s " + getString("maxflux_less"), pad, add, Math.round((flux -1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("disflux_less"), pad, add, Math.round((flux -1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("maintanence_increase"), pad, add, Math.round(((SUPPLY_USE_MULT - 1f) * 100f)) + "%");
        tooltip.addPara("%s " + getString("fuel_perly_more"), pad, add, Math.round((FUEL_TRAVEL -1f)) + "");
        tooltip.addPara("%s " + getString("less_speed"), pad, add, Math.round((shpeed - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("less_turn"), pad, add, Math.round((shpeed - 1f) * -100f) + "%");
        tooltip.addPara("%s", pad, add, "This hullmod disables shield completly.");


        tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
        TooltipMakerAPI blocked = tooltip.beginImageWithText(HullmodIncompatible, 40);
            blocked.addPara(getString("fuckyou"), pad);
                
            blocked.addPara("- This hullmod blocks too much hullmods so I am lazy to write them all down.", Misc.getNegativeHighlightColor(), pad);
            
        tooltip.addImageWithText(pad);
    }

    //Bork

}


