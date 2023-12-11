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

import data.hullmods.DermondBlockedHullmodDisplayScript;

import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.EnergyWeaponMastery;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import data.scripts.utils.dalton_utils;

import java.util.HashSet;
import java.util.Set;

public class DermondEnginePower extends BaseHullMod {

    private String getString(String key) {
        return Global.getSettings().getString("der",  key);}

    //Positive
    public static float SPEED = 30f;

    public static float MANEUVARABILITY = 15f;



    //Negative
    public static float nerf = 15f;


    //Blocked
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(6);

    static
    {
        // These hullmods will automatically be removed
        // Not as elegant as blocking them in the first place, but
        // this method doesn't require editing every hullmod's script
        BLOCKED_HULLMODS.add("vic_assault");
        BLOCKED_HULLMODS.add("mhmods_hyperengineupgrade");
        BLOCKED_HULLMODS.add("auxiliarythrusters");
        BLOCKED_HULLMODS.add("insulatedengine");
        BLOCKED_HULLMODS.add("safetyoverrides");
        BLOCKED_HULLMODS.add("unstable_injector");
        //BLOCKED_HULLMODS.add("");
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxSpeed().modifyPercent(id, SPEED);
        stats.getTurnAcceleration().modifyPercent(id, MANEUVARABILITY);
        stats.getShieldArcBonus().modifyFlat(id, -nerf);
        stats.getTimeMult().modifyMult(id, 1.15f);
    }

    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        if(Global.getCurrentState() != GameState.TITLE) {
            Map<String, Object> data = Global.getSector().getPersistentData();
            if (!data.containsKey("aiengine_check_" + member.getId())) {
                data.put("aiengine_check_" + member.getId(), "_");
                if (member.getFleetData() != null && member.getFleetData().getFleet() != null && member.getFleetData().getFleet().equals(Global.getSector().getPlayerFleet())) {
                    dalton_utils.removePlayerCommodity("fuel", 250);
                }
            }
            if (!member.getVariant().hasHullMod("der_holder")) {
                member.getVariant().getHullMods().add("der_holder");
            }
        }
    }




    public boolean canBeAddedOrRemovedNow(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        if(ship.getVariant().hasHullMod("DermondEnginePower")){
            return true;
        }else{
            return dalton_utils.playerHasCommodity("fuel", 250) && super.canBeAddedOrRemovedNow(ship, marketOrNull, mode);
        }
    }

    public String getCanNotBeInstalledNowReason(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        return !dalton_utils.playerHasCommodity("fuel", 250) ? "You do not have the required amount of fuel." : super.getCanNotBeInstalledNowReason(ship, marketOrNull, mode);
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
        String fuel = "graphics/icons/cargo/fuel.png";
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI DermondIcon = tooltip.beginImageWithText(DermondCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        DermondIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        DermondIcon.addPara("%s", 6f, flavor, getString("speed_desc")); //Main text
        DermondIcon.addPara("%s", 1f, flavor, getString("random_guy")); // Author


        tooltip.addImageWithText(PAD);


        //Positive bonuses
        tooltip.addPara("%s " + getString("speed_increase"), pad, arr, Math.round(SPEED) + "%"  );
        tooltip.addPara("%s " + getString("maneuv_increase"), pad, arr, Math.round(MANEUVARABILITY) + "%"  );


        //Negative ones
        tooltip.addPara("%s " + getString("shieldarc_less"), pad, add, Math.round(nerf) + " degree");


        
        tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
        TooltipMakerAPI blocked = tooltip.beginImageWithText(HullmodIncompatible, 40);
        blocked.addPara(getString("fuckyou"), pad);
            
        blocked.addPara("- Insulated Engine Assembly", Misc.getNegativeHighlightColor(), pad);
        blocked.addPara("- Safety Overrides", Misc.getNegativeHighlightColor(), pad);
        blocked.addPara("- Auxiliary Thrusters", Misc.getNegativeHighlightColor(), pad);
        blocked.addPara("- Unstable Injector", Misc.getNegativeHighlightColor(), pad);
        if (Global.getSettings().getModManager().isModEnabled("more_hullmods")) {
            blocked.addPara(" - Hyper Engine Upgrade", Misc.getNegativeHighlightColor(), pad);
        }
        if (Global.getSettings().getModManager().isModEnabled("vic")) {
            blocked.addPara(" - Inertia Redirection Systems", Misc.getNegativeHighlightColor(), pad);
        }
        tooltip.addImageWithText(pad);

        tooltip.addSectionHeading("Hullmod Cost", Alignment.MID, pad);
        TooltipMakerAPI cost = tooltip.beginImageWithText(fuel, 25);
        cost.addPara("- 250 fuel is needed to install this hullmod", Misc.getHighlightColor(), pad);
        tooltip.addImageWithText(pad);
        tooltip.addPara("Attention, after installing said hullmod, all commodities needed to install will disapear. " +
                "This does not count Crew and Marines, as they run under different equation", Misc.getNegativeHighlightColor(), pad);
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