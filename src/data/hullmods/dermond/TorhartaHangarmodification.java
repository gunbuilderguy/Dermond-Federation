package data.hullmods.dermond;

//Some of these you don't need, but I will just keep them here just in case

import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.jcraft.jorbis.Block;
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
import data.scripts.utils.dalton_utils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;




public class TorhartaHangarmodification extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    //Positive effects
    public static final float replace_time_wing = 0.85f;
    public static final float reduce_wing_casualties = 0.8f;


    //Negative effects
    public static final float more_crew_fighter = 35f;


    /*
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(x);
    static
    {
        BLOCKED_HULLMODS.add("armoredweapons");

    }*/


    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //Positive
        stats.getFighterRefitTimeMult().modifyMult(id, replace_time_wing);
		stats.getDynamic().getStat(Stats.FIGHTER_CREW_LOSS_MULT).modifyMult(id, reduce_wing_casualties);
        

        //Negative
        stats.getMinCrewMod().modifyPercent(id, more_crew_fighter);

    }
/* if I finish blocks this is unlocked
    @Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        for (String tmp : BLOCKED_HULLMODS)
        {
            if (ship.getVariant().getHullMods().contains(tmp))
            {
                ship.getVariant().removeMod(tmp);
                DermondBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
	}
*/
    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        if(Global.getCurrentState() != GameState.TITLE) {
            Map<String, Object> data = Global.getSector().getPersistentData();
            if (!data.containsKey("aitorthata_check_" + member.getId())) {
                data.put("aitortahta_check_" + member.getId(), "_");
                if (member.getFleetData() != null && member.getFleetData().getFleet() != null && member.getFleetData().getFleet().equals(Global.getSector().getPlayerFleet())) {
                    dalton_utils.removePlayerCommodity("supplies", 200);
                }
            }
            if (!member.getVariant().hasHullMod("der_holder")) {
                member.getVariant().getHullMods().add("der_holder");
            }
        }
    }


    public boolean canBeAddedOrRemovedNow(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        if(ship.getVariant().hasHullMod("DermondTorhartaHangarmodification")){
            return true;
        }else{
            return dalton_utils.playerHasCommodity("supplies", 200) && super.canBeAddedOrRemovedNow(ship, marketOrNull, mode);
        }
    }

    public String getCanNotBeInstalledNowReason(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        return !dalton_utils.playerHasCommodity("supplies", 200) ? "You do not have the required ammount of supplies" : super.getCanNotBeInstalledNowReason(ship, marketOrNull, mode);
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
        OrdoIcon.addPara("%s", 6f, flavor, getString("dermond_figher_crew_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("dermond_legend_astral_guy")); // Author


        tooltip.addImageWithText(PAD);
        //Boobies at nheantai.net :)

        //Positive bonuses
        tooltip.addPara("%s " + getString("replace_wing"), pad, arr, Math.round((replace_time_wing -1f) * -100f) + "%" );
        tooltip.addPara("%s " + getString("reduced casualties"), pad, arr, Math.round((reduce_wing_casualties -1f) * -100f) + "%" );

        //Negative bonuses
        tooltip.addPara("%s " + getString("more_crew_on_ship"), pad, add, Math.round(more_crew_fighter) + "%" );


        /*
        tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
        TooltipMakerAPI blocked = tooltip.beginImageWithText(HullmodIncompatible, 40);
            blocked.addPara(getString("fuckyou"), pad);
                
            blocked.addPara("- ", Misc.getNegativeHighlightColor(), pad);
        */
    }

    //DOOD!

	public boolean isApplicableToShip(ShipAPI ship) {
		//if (ship.getMutableStats().getCargoMod().computeEffective(ship.getHullSpec().getCargo()) < CARGO_REQ) return false;
		
		return ship != null && ship.getHullSpec().getFighterBays() > 0 &&
								//ship.getNumFighterBays() <= 0 &&
								!ship.getVariant().hasHullMod(HullMods.CONVERTED_BAY) &&
								!ship.getVariant().hasHullMod(HullMods.PHASE_FIELD);
								//ship.getHullSpec().getShieldType() != ShieldType.PHASE;		
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.getHullSpec().getFighterBays() <= 0) return "Ship has no standard fighter bays";
		if (ship != null && ship.getVariant().hasHullMod(HullMods.CONVERTED_BAY)) return "Ship has no standard fighter bays";
		//if (ship != null && ship.getNumFighterBays() > 0) return "Ship has fighter bays";
		return "Cannot be installed on a phase ship";
	}
}



