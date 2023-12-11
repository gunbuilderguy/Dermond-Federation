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
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.HullDamageAboutToBeTakenListener;
import data.scripts.utils.dalton_utils;

import java.util.HashSet;
import java.util.Set;

public class DermondFertillaFlux extends BaseHullMod {

    private String getString(String key) {
        return Global.getSettings().getString("der",  key);}

    //Positive
	private static Map<HullSize, Float> max_flux = new HashMap();
	static {
	    max_flux.put(HullSize.FRIGATE, 10f);
		max_flux.put(HullSize.DESTROYER, 20f);
		max_flux.put(HullSize.CRUISER, 30f);
		max_flux.put(HullSize.CAPITAL_SHIP, 40f);
    }

	private static Map<HullSize, Float> flux_dis = new HashMap();
    static {
        flux_dis.put(HullSize.FRIGATE, 40f);
        flux_dis.put(HullSize.DESTROYER, 30f);
        flux_dis.put(HullSize.CRUISER, 20f);
        flux_dis.put(HullSize.CAPITAL_SHIP, 10f);
    }

    //Negative
    public static int SUPPLY_USE_MULT = 50;

    public static final float CR_DEGRADATION = 1.6f;


    //Blocked
    //Also don't forget to change that number there!
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(5);

    static
    {
        // These hullmods will automatically be removed
        // Not as elegant as blocking them in the first place, but
        // this method doesn't require editing every hullmod's script
        BLOCKED_HULLMODS.add("fluxcoil");
        BLOCKED_HULLMODS.add("fluxdistributor");
        BLOCKED_HULLMODS.add("effcency_overhaul");
        BLOCKED_HULLMODS.add("yunru_fast_flux");
        BLOCKED_HULLMODS.add("yunru_big_flux");
        //BLOCKED_HULLMODS.add("");
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getTimeMult().modifyPercent(id, 20f); //Yes this effect will not be written in the description. And I don't care!
        stats.getFluxCapacity().modifyPercent(id, (float) max_flux.get(hullSize));
        stats.getFluxDissipation().modifyPercent(id, (float) flux_dis.get(hullSize));
        stats.getSuppliesPerMonth().modifyPercent(id, SUPPLY_USE_MULT);
        stats.getCRLossPerSecondPercent().modifyMult(id, CR_DEGRADATION);
    }


    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        if(Global.getCurrentState() != GameState.TITLE) {
            Map<String, Object> data = Global.getSector().getPersistentData();
            if (!data.containsKey("aifertilla_check_" + member.getId())) {
                data.put("aifertilla_check_" + member.getId(), "_");
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
        if(ship.getVariant().hasHullMod("DermondFertillaFlux")){
            return true;
        }else{
            return dalton_utils.playerHasCommodity("supplies", 200) && super.canBeAddedOrRemovedNow(ship, marketOrNull, mode);
        }
    }

    public String getCanNotBeInstalledNowReason(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        return !dalton_utils.playerHasCommodity("supplies", 200) ? "You do not have the required amount of supplies" : super.getCanNotBeInstalledNowReason(ship, marketOrNull, mode);
    }

    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);
		String HullmodIncompatible = "graphics/icons/tooltips/der_hullmod_incompatible.png"; //what hullmod to show				
        String CSTitle = "'Dermondian Engieneering'"; //Title
        String DermondCrest = "graphics/factions/crest_Dermond_Federation.png"; //What thing to display near the qoute
        String supplies = "graphics/icons/cargo/supplies.png";
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()}; //Green color
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()}; //Red color
        TooltipMakerAPI DermondIcon = tooltip.beginImageWithText(DermondCrest, HEIGHT); //idfk


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        DermondIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        DermondIcon.addPara("%s", 6f, flavor, getString("flux_desc")); //Main text
        DermondIcon.addPara("%s", 1f, flavor, getString("Ornium")); // Author


        tooltip.addImageWithText(PAD);

        //Positive bonuses
        tooltip.addPara("%s " + getString("maxflux_increase"), pad, arr, Math.round(max_flux.get(hullSize)) + "%"  );
        tooltip.addPara("%s " + getString("fluxdis_increase"), pad, arr, Math.round(flux_dis.get(hullSize)) + "%"  );


        //Negative ones
        tooltip.addPara("%s " + getString("maintanence_increase"), pad, add, SUPPLY_USE_MULT + "%");
        tooltip.addPara("%s " + getString("degradecr_fast"), pad, add, Math.round((CR_DEGRADATION - 1f) * 100f) + "%");
        


        
    tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
    TooltipMakerAPI blocked = tooltip.beginImageWithText(HullmodIncompatible, 40);
        blocked.addPara(getString("fuckyou"), pad);
            
        blocked.addPara("- Flux coil", Misc.getNegativeHighlightColor(), pad);
        blocked.addPara("- Flux Distrubitore", Misc.getNegativeHighlightColor(), pad);
        blocked.addPara("- Efficeny overhaul", Misc.getNegativeHighlightColor(), pad);
        if (Global.getSettings().getModManager().isModEnabled("yunruhullmods")) {
            blocked.addPara(" - Fast flux conduits", Misc.getNegativeHighlightColor(), pad);
            blocked.addPara(" - High capacity flux", Misc.getNegativeHighlightColor(), pad);
        }
        tooltip.addImageWithText(pad);

        tooltip.addSectionHeading("Hullmod Cost", Alignment.MID, pad);
        TooltipMakerAPI cost = tooltip.beginImageWithText(supplies, 25);
        cost.addPara("- 200 supplies is needed to install this hullmod", Misc.getHighlightColor(), pad);
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