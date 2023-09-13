package data.hullmods.dermond;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.HashMap;
import java.util.Map;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.Alignment;
import java.awt.Color;
import com.fs.starfarer.api.combat.ShieldAPI;
import data.scripts.utils.dalton_utils;


public class CeltorianSystem extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    
    //positive
    public static final float phase_cooldown = 0.9f;
    public static final float phase_cost_per_second = 0.95f;
    //Untill I find a way how to increase speed in phase I will just leave you here
    //public static final float phase_move = 1.2f;


    //negative
    public static final float phase_cost_in = 1.45f;
    private static Map<HullSize, Float> CRDEGRADATION = new HashMap();
    static {
        CRDEGRADATION.put(HullSize.FRIGATE, 20f);
        CRDEGRADATION.put(HullSize.DESTROYER, 40f);
        CRDEGRADATION.put(HullSize.CRUISER, 80f);
        CRDEGRADATION.put(HullSize.CAPITAL_SHIP, 200f);
    }


    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getPhaseCloakCooldownBonus().modifyMult(id, phase_cooldown);
        stats.getPhaseCloakUpkeepCostBonus().modifyMult(id, phase_cost_per_second);

        stats.getPhaseCloakActivationCostBonus().modifyMult(id, phase_cost_in);
    }

    @Override
	public boolean isApplicableToShip(ShipAPI ship) {
		//return ship != null && ship.getShield() == null && ship.getPhaseCloak() == null;
		return ship.getHullSpec().isPhase();
    }

    //nuke: test if this is properly incompatible with reactive fields
        
    //another note: Vanguards are annoying even out of the game

    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        if(Global.getCurrentState() != GameState.TITLE) {
            Map<String, Object> data = Global.getSector().getPersistentData();
            if (!data.containsKey("aiceltorian_check_" + member.getId())) {
                data.put("aiceltorian_check_" + member.getId(), "_");
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
        if(ship.getVariant().hasHullMod("DermondCeltorianSystem")){
            return true;
        }else{
            return dalton_utils.playerHasCommodity("supplies", 200) && super.canBeAddedOrRemovedNow(ship, marketOrNull, mode);
        }
    }

    public String getCanNotBeInstalledNowReason(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        return !dalton_utils.playerHasCommodity("supplies", 200) ? "You do not have the required ammount of supplies" : super.getCanNotBeInstalledNowReason(ship, marketOrNull, mode);
    }
                
    public String getUnapplicableReason(ShipAPI ship) {
		
		if (ship.getHullSpec().getDefenseType() == ShieldAPI.ShieldType.FRONT) {
            return "Ship has no phase coils";
        }
        if (ship.getHullSpec().getDefenseType() == ShieldAPI.ShieldType.OMNI) {
            return "Ship has no phase coils";
        }
        if (ship.getHullSpec().getDefenseType() == ShieldAPI.ShieldType.NONE) {
            return "Ship has no phase coils";
        }
            
		return null;
	}

    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);			
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
        DermondIcon.addPara("%s", 6f, flavor, getString("dermond_phase_desc")); //Main text
        DermondIcon.addPara("%s", 1f, flavor, getString("dermond_legend_phase_guy")); // Author

        tooltip.addImageWithText(PAD);

        //Positive bonuses
        tooltip.addPara("%s " + getString("phase_cooldown_decrease"), pad, arr, Math.round((phase_cooldown - 1f) * -100) + "%"  );
        tooltip.addPara("%s " + getString("phase_asecond_decrease"), pad, arr, Math.round((phase_cost_per_second - 1f) * -100) + "%");

        //Negative ones
        tooltip.addPara("%s " + getString("degradecr_fast"), pad, add, Math.round(CRDEGRADATION.get(hullSize)) + "%");
        tooltip.addPara("%s " + getString("phase_cost_in"), pad, add, Math.round((phase_cost_in - 1f) * 100) + "%");
    }

}