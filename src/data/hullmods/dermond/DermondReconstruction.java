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
import com.fs.starfarer.api.impl.campaign.skills.EnergyWeaponMastery;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashSet;
import java.util.Set;

public class DermondReconstruction extends BaseHullMod {

    private String getString(String key) {
        return Global.getSettings().getString("der", key);}


    //Positive bonuses
    public static final float ARMOR_BONUS = 1.20f; //Armor 20% bonus
    public static final float HIGHEXPLOSIVERE_DAMAGE_REDUCTION = 0.9f; //HE reduction by 10%
    public static final float SHIELD_EFFICENCY = 0.85f; // Shield effcency increased by15%

    //Negative bonuses
    public static final float HULL_BONUS = 0.75f; //Hull decreased by 25%
    public static final float ENERGY_DAMAGE_INCREASE = 1.25f; // Energy damage taken increased by 5%
    public static final float SUPPLY_USE_MULT = 1.5f; //Maintanance increased by 50%

    //Blocked hullmods, thx s0ren for figuring out how to do it
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(7);

    static
    {
        // These hullmods will automatically be removed
        // Not as elegant as blocking them in the first place, but
        // this method doesn't require editing every hullmod's script
        BLOCKED_HULLMODS.add("heavyarmor");
        BLOCKED_HULLMODS.add("apex_armor");
        BLOCKED_HULLMODS.add("apex_cryo_armor");
        BLOCKED_HULLMODS.add("converted_hangar");
        BLOCKED_HULLMODS.add("roider_fighterClamps");
        BLOCKED_HULLMODS.add("tahlan_daemonplating");
        BLOCKED_HULLMODS.add("vic_PDArcEmitter");
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getHighExplosiveDamageTakenMult().modifyMult(id, HIGHEXPLOSIVERE_DAMAGE_REDUCTION);
        stats.getArmorBonus().modifyMult(id, ARMOR_BONUS);
        stats.getShieldDamageTakenMult().modifyMult(id, SHIELD_EFFICENCY);


        stats.getHullBonus().modifyMult(id, HULL_BONUS);
        stats.getEnergyDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_INCREASE);
        stats.getSuppliesPerMonth().modifyMult(id, SUPPLY_USE_MULT);

        /*
        if(Global.getSettings().getWeaponSpec(weapon).hasTag("Dermond")) {

            stats.getDynamic().getMod(Stats.LARGE_BALLISTIC_MOD).modifyFlat(id, 5);
            stats.getDynamic().getMod(Stats.LARGE_ENERGY_MOD).modifyFlat(id, 5);
            stats.getDynamic().getMod(Stats.LARGE_MISSILE_MOD).modifyFlat(id, 5);

            stats.getDynamic().getMod(Stats.MEDIUM_BALLISTIC_MOD).modifyFlat(id, 5);
            stats.getDynamic().getMod(Stats.MEDIUM_ENERGY_MOD).modifyFlat(id, 5);
            stats.getDynamic().getMod(Stats.MEDIUM_MISSILE_MOD).modifyFlat(id, 5);

            stats.getDynamic().getMod(Stats.SMALL_BALLISTIC_MOD).modifyFlat(id, 5);
            stats.getDynamic().getMod(Stats.SMALL_ENERGY_MOD).modifyFlat(id, 5);
            stats.getDynamic().getMod(Stats.SMALL_MISSILE_MOD).modifyFlat(id, 5);

        }*/

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
        String DermondCrest = "graphics/factions/crest_Dermond_Federation_messedup.png";
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI DermondIcon = tooltip.beginImageWithText(DermondCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        DermondIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        DermondIcon.addPara("%s", 6f, flavor, getString("reconstruction_desc")); //Main text
        DermondIcon.addPara("%s", 1f, flavor, getString("random_guy")); // Author


        tooltip.addImageWithText(PAD);


        //Positive bonuses


        tooltip.addPara("%s " + getString("armour_bonus"), pad, arr, Math.round((1f - ARMOR_BONUS) * -100f) + "%");
        tooltip.addPara("%s " + getString("highexplose_reduction"), pad, arr, Math.round((1f - HIGHEXPLOSIVERE_DAMAGE_REDUCTION) * 100f) + "%");
        tooltip.addPara("%s " + getString("shield_efficent"), pad, arr, Math.round((1f - SHIELD_EFFICENCY) * 100f) + "%");

        //Negative ones
        tooltip.addPara("%s " + getString("hull_decrease"), pad, add, Math.round((1f - HULL_BONUS)  * 100f) + "%");
        tooltip.addPara("%s " + getString("degradecr_fast"), pad, add, Math.round((1f - ENERGY_DAMAGE_INCREASE) * -100f) + "%");
        tooltip.addPara("%s " + getString("maintanence_increase"), pad, add, Math.round((1f - SUPPLY_USE_MULT) * -100f) + "%");


        
    tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
    TooltipMakerAPI blocked = tooltip.beginImageWithText(HullmodIncompatible, 40);
        blocked.addPara(getString("fuckyou"), PAD);
            
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