package data.hullmods.special;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.StatBonus;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.utils.dalton_utils;
import java.awt.*;
import java.awt.Color;
import com.jcraft.jorbis.Block;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;


/*

Special thanks to Dalton, for allowing to use his code

And yes this is op af
⢀⠐⠠⢂⠰⠀⢆⠢⡐⠢⡐⢢⢐⡂⢖⡤⢳⡜⡲⡰⢠⡀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠠⢈⠁⠂⡄⠉⡄⢢⠡⡑⢤⢃⠮⣜⢣⢞⡱⢎⡵⣩⠳⣬⢱⠂⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢀⠂⠌⡐⢠⠑⡄⣃⠦⣱⢪⡝⡺⣌⠳⣎⠵⣋⠴⣃⠟⡴⣋⡝⡢⢄⡀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠌⡐⢌⠰⢌⠲⣥⢛⡴⢣⠞⡱⣌⢳⠸⣘⢆⢳⡘⢎⡕⠮⣜⡱⢎⡴⢃⡄⠠⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢈⠰⠐⡌⢢⢍⡞⡔⡫⢔⠫⡜⡱⢌⠲⣉⠖⣪⠱⡘⢎⡜⡱⣊⢜⡱⢌⠳⣘⢣⢣⠜⡠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢄⢊⡱⢌⢣⡚⣬⠱⣡⠋⢆⠱⡑⢪⠱⣡⠚⢥⢣⡙⢦⡘⡱⠌⠦⡑⢊⡱⢌⠣⢎⡝⢦⡑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢂⠎⡴⣉⠖⣱⢢⠓⡤⡉⢆⠱⡈⢆⠱⢂⠝⣂⠧⣘⠢⡑⢌⡘⢡⠘⡄⠒⡌⡱⢩⡜⢦⡱⢅⡂⠄⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⠀⠀
⡌⡚⠔⡡⠚⡄⢣⡙⠔⡱⡈⢆⠱⢈⠢⢉⠒⡌⢒⡡⢒⡉⢆⡘⢄⠣⢌⠱⢠⠃⢧⡘⠦⡱⢩⠜⣌⠣⡜⣠⠡⣀⠂⠀⠀⠀⠀⠀⠀⠀
⠔⡁⠊⢀⠁⠠⠁⡘⠌⡡⠱⡈⠆⣁⠢⢁⠎⡰⢁⠆⢣⡘⠤⡘⢄⠣⢌⠢⡁⠞⡠⠜⡐⢁⠃⠌⠀⠓⡘⠤⢓⡄⢂⠀⠀⠀⠀⠀⠀⠀
⡘⢄⡐⠀⠀⠀⠀⠀⠂⠡⢁⠘⡀⠆⠰⠡⢘⠠⢃⠬⢡⠜⣠⠑⡌⡐⠢⢑⡈⠆⡁⠂⠀⠀⠀⠀⠀⠀⠠⢉⠒⡬⢡⢂⠄⡀⠀⠀⠀⠀
⡑⠢⢄⠃⢌⠠⢀⠀⠀⠀⠀⠂⠐⠈⠄⠡⢂⠱⢈⠆⡡⠎⣄⠣⡐⠡⢁⠂⡐⠠⠐⠀⠀⠀⠀⠀⠀⠀⢀⠢⡑⢌⡃⢎⠲⡡⢎⡐⠀⠀
⣁⠃⠢⠌⢂⠒⡀⢂⡐⠠⠀⡀⠈⠐⡈⢡⠂⡅⢊⠤⡑⠌⡤⢃⠌⡡⢂⠐⠀⠀⠀⠀⠀⠀⠀⢀⠠⡐⢢⠡⡘⠤⡘⢌⢣⡑⢎⡜⠡⠀
⠠⠑⠂⠌⡀⠆⡐⢠⢀⡁⠒⢠⠁⠆⢡⠂⡜⠠⢃⠒⡌⢒⡰⢁⠎⡐⠤⡈⢄⠡⠌⡐⢂⠱⡈⠆⡑⠌⡂⠱⠈⠆⡑⠨⢠⠘⡰⢌⡓⢌
⠅⢊⠡⢂⠥⡘⢄⠃⡆⢌⡑⢂⡉⡘⠄⡡⢂⠱⣈⠒⡌⢡⠒⡈⠔⡡⢢⠱⣈⠦⡑⢌⢂⠡⡐⠡⢌⡐⠌⡡⢑⡈⠔⢡⠂⢢⠑⡌⠜⡤
⠌⢂⠱⡈⠔⡌⡌⠒⡌⠢⠌⠠⠐⢀⠂⡁⠂⢆⡀⠣⡘⢄⠊⡔⢡⠂⡅⢒⠡⢂⠱⡈⢆⠓⡌⣑⠢⡘⢤⠑⢢⠘⠌⣂⠑⢢⠘⣀⠣⡐
⡘⠄⢣⠘⣐⠢⡘⢡⠂⢁⠠⠀⢀⠂⠠⢀⠑⡠⠐⠡⢘⠠⢃⠜⡠⢑⡈⠆⠱⡈⠆⢡⠈⠆⡱⢈⠥⡙⠤⢋⠔⣈⠐⠠⠉⡄⠒⣀⠂⠡
⠠⠘⡀⠣⢄⠣⠘⠠⠈⠀⠀⠀⡀⢀⠂⠄⠂⠄⠡⠈⠄⢂⠁⢂⡐⢂⠐⡈⠡⡐⡉⠄⡈⠐⠠⢁⠢⠉⠆⠡⠊⠄⠌⡡⠘⢠⠁⠄⡈⢀
⠀⢂⠐⣁⠢⢁⠁⠂⠀⠐⠈⢀⠀⡀⠀⠈⠀⡈⠀⢁⠈⠀⠌⠠⠐⢀⠂⠄⢡⠐⠌⠠⠁⠌⠀⠀⠠⠁⠌⠡⢈⡐⠈⠄⡑⠠⢈⠐⡀⠆
⠐⡀⠂⠄⠂⠄⠈⠀⠀⠂⠁⠀⠄⠀⡀⠀⠀⠀⠐⠀⠀⠈⢀⠐⠀⡀⠂⠌⡀⠌⠠⠁⠂⠀⠀⠁⢀⠀⠠⠁⠂⠠⠈⠐⠀⠁⠀⠀⠀⠀
⠀⠄⠁⡈⠐⡈⠐⡀⠀⠀⠀⠈⠀⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠂⢀⠠⠀⠀⠀⠀⡀⠌⢀⠐⡀⠄⠂⢁⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠄⠁⡀⠐⠀⠀⠄⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠈⠀⠄⡁⠂⠀⠀⠀⠄⠁⠠⠐⠀⠂⢀⠀⠐⠠⢈⠐⠠⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠐⠀⡐⠀⠀⡁⠠⠐⡀⠄⠀⡀⠀⠀⠄⢂⠀⢂⠡⢐⠠⠀⠀⠀⠠⠀⢈⠀⡐⠀⠁⠀⠀⠀⡁⠂⢈⠡⢊⠄⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠂⢀⠠⠀⠀⠂⡐⠠⢀⠂⡔⠀⢌⠸⣄⠊⡄⢂⠇⠠⠀⠀⠀⠁⡀⠂⠀⠀⠀⠀⠀⠀⠀⠠⠐⠀⠌⣡⠒⡌⡀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠠⠑⡀⢣⠜⡈⢆⡳⢌⠒⠈⠀⠈⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠐⢠⠫⡔⡡⢎
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠃⢊⠑⠈⠒⠉⠀⠀⠀⠀⢀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⢇⡣⢕⢪

 */

public class DermondHunterAI_Intergration extends BaseHullMod {

    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    public static final float range = 3f;
    public static final float accuracy = 40f;
    public static final float pddmg = 90f;
    public static final float flux = 50f;
    public static final float sped = 75f;
    public static final float shields = 0.75f;

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //Yes this AI is op af, but you know it's just now. In the main update he will be an NPC and if you will have this versioon I will crashcode your fucking mind NGO style :>

        stats.getBeamPDWeaponRangeBonus().modifyMult(id, range);
        stats.getEnergyWeaponRangeBonus().modifyMult(id, range);
        stats.getBallisticWeaponRangeBonus().modifyMult(id, range);
        stats.getNonBeamPDWeaponRangeBonus().modifyMult(id, range);
    
        stats.getAutofireAimAccuracy().modifyPercent(id, accuracy);

        stats.getDamageToMissiles().modifyPercent(id, pddmg);
        stats.getDamageToFighters().modifyPercent(id, pddmg);

        stats.getFluxCapacity().modifyPercent(id, flux);
        stats.getFluxDissipation().modifyPercent(id, flux);

        stats.getTurnAcceleration().modifyPercent(id, sped);
        stats.getMaxTurnRate().modifyPercent(id, sped);
        stats.getMaxSpeed().modifyPercent(id, sped);

        stats.getShieldAbsorptionMult().modifyMult(id, shields);
    }

    //Copied from epta 2.0.0

    public void advanceInCampaign(FleetMemberAPI member, float amount) {

        Map<String, Object> data = Global.getSector().getPersistentData();
        if (!data.containsKey("aiinthunter_check_" + member.getId())) {
            data.put("aiinthunter_check_" + member.getId(), "_");
            if(member.getFleetData() != null && member.getFleetData().getFleet() != null && member.getFleetData().getFleet().equals(Global.getSector().getPlayerFleet())) {
                dalton_utils.removePlayerCommodity("dermond_hunter_AI");
            }
        }

        if(!member.getVariant().hasHullMod("DermondHunterAI_return")){
            member.getVariant().getHullMods().add("DermondHunterAI_return");
        }
    }
    
    public String getUnapplicableReason(ShipAPI ship) {
        boolean hasai = false;
        for(String hullmod:ship.getVariant().getHullMods()){
            if(Global.getSettings().getHullModSpec(hullmod).hasTag("AIIntegration") && !hullmod.equals("DermondHunterAI_Intergration")){
                hasai = true;
            }
        }

        if (hasai){
            return "Only one integrated AI is able to be installed at once";
        }

        return null;
    }

    public boolean canBeAddedOrRemovedNow(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        if(ship.getVariant().hasHullMod("DermondHunterAI_Intergration")){
            return true;
        }else{
            return dalton_utils.playerHasCommodity("dermond_hunter_AI") && super.canBeAddedOrRemovedNow(ship, marketOrNull, mode);
        }
    }

    public String getCanNotBeInstalledNowReason(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        return !dalton_utils.playerHasCommodity("dermond_hunter_AI") ? "You do not have the required AI core" : super.getCanNotBeInstalledNowReason(ship, marketOrNull, mode);
    }
    

    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);			
        String CSTitle = "'Tri Tachyon Corporation Experimental Engieneering'";
        String OrdoCrest ="graphics/factions/crest_hunter.png" ;
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("hunterai_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("hunter_ai")); // Author



        tooltip.addImageWithText(PAD);

        //Positive bonuses
        tooltip.addPara("%s " + getString("range_increase"), pad, arr, Math.round((range - 1f) * 100f) + "%"); 
        tooltip.addPara("%s " + getString("accuracy_increase"), pad, arr, Math.round(accuracy ) + "%");
        tooltip.addPara("%s " + getString("increase_pddmg"), pad, arr, Math.round(pddmg) + "%");
        tooltip.addPara("%s " + getString("maxflux_increase"), pad, arr, Math.round(flux) + "%");
        tooltip.addPara("%s " + getString("fluxdis_increase"), pad, arr, Math.round(flux) + "%");
        tooltip.addPara("%s " + getString("speed_increase"), pad , arr, Math.round(sped) + "%");
        tooltip.addPara("%s " + getString("maneuv_increase"), pad, arr, Math.round(sped) + "%");
        tooltip.addPara("%s " + getString("shield_efficent"), pad, arr, Math.round((shields - 1f) * -100f) + "%");


    
    }

    //I HATE PEOPLE!
}
