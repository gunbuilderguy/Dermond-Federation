package data.hullmods.ballers;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.Alignment;
import java.awt.Color;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;


/*

swigity swooty, you get oneshoted

⢿⣿⣿⣿⣭⠹⠛⠛⠛⢿⣿⣿⣿⣿⡿⣿⠷⠶⠿⢻⣿⣛⣦⣙⠻⣿
⣿⣿⢿⣿⠏⠀⠀⡀⠀⠈⣿⢛⣽⣜⠯⣽⠀⠀⠀⠀⠙⢿⣷⣻⡀⢿
⠐⠛⢿⣾⣖⣤⡀⠀⢀⡰⠿⢷⣶⣿⡇⠻⣖⣒⣒⣶⣿⣿⡟⢙⣶⣮
⣤⠀⠀⠛⠻⠗⠿⠿⣯⡆⣿⣛⣿⡿⠿⠮⡶⠼⠟⠙⠊⠁⠀⠸⢣⣿
⣿⣷⡀⠀⠀⠀⠀⠠⠭⣍⡉⢩⣥⡤⠥⣤⡶⣒⠀⠀⠀⠀⠀⢰⣿⣿
⣿⣿⡽⡄⠀⠀⠀⢿⣿⣆⣿⣧⢡⣾⣿⡇⣾⣿⡇⠀⠀⠀⠀⣿⡇⠃
⣿⣿⣷⣻⣆⢄⠀⠈⠉⠉⠛⠛⠘⠛⠛⠛⠙⠛⠁⠀⠀⠀⠀⣿⡇⢸
⢞⣿⣿⣷⣝⣷⣝⠦⡀⠀⠀⠀⠀⠀⠀⠀⡀⢀⠀⠀⠀⠀⠀⠛⣿⠈
⣦⡑⠛⣟⢿⡿⣿⣷⣝⢧⡀⠀⠀⣶⣸⡇⣿⢸⣧⠀⠀⠀⠀⢸⡿⡆
⣿⣿⣷⣮⣭⣍⡛⠻⢿⣷⠿⣶⣶⣬⣬⣁⣉⣀⣀⣁⡤⢴⣺⣾⣽⡇

 */
public class bigballs extends BaseHullMod {


    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }
    public static final float BigProblemo = 20000f; // Damage taken %
    public static final float moarfluxtsussy = 100f; // more flux overall %
    public static final float shieldophaso = 100f; // shield eff and phase cost. Phase cost count like this = shiledophaso / 2f
    public static final float xivspiritsupport = 75f; // speed and turn %
    public static final float gayso = 70f; // i don't rememeber


    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

        stats.getHullDamageTakenMult().modifyPercent(id, BigProblemo);
        stats.getArmorDamageTakenMult().modifyPercent(id, BigProblemo);
        stats.getEmpDamageTakenMult().modifyPercent(id, BigProblemo);

        stats.getFluxCapacity().modifyPercent(id, moarfluxtsussy);
        stats.getFluxDissipation().modifyPercent(id, moarfluxtsussy);

        stats.getShieldAbsorptionMult().modifyPercent(id, shieldophaso);
        stats.getPhaseCloakCooldownBonus().modifyPercent(id, (shieldophaso / 2));

        stats.getMaxSpeed().modifyPercent(id, xivspiritsupport);
        stats.getTurnAcceleration().modifyPercent(id, xivspiritsupport);
        stats.getMaxTurnRate().modifyPercent(id, xivspiritsupport);

        stats.getPeakCRDuration().modifyPercent(id, gayso);
    }

    public String getUnapplicableReason(ShipAPI ship) {
        boolean hasai = false;
        for(String hullmod:ship.getVariant().getHullMods()){
            if(Global.getSettings().getHullModSpec(hullmod).hasTag("ballerinc") && !hullmod.equals("dermondbigballs")){
                hasai = true;
            }
        }

        if (hasai){
            return "Only one Baller INC hullmods can be installed on the ship";
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
        String CSTitle = "Baller Incorporated Magic?";
        String OrdoCrest ="graphics/factions/crest_dermond_ballerinc.png" ;
		float pad = 2f;		
        String HullmodIncompatible = "graphics/icons/tooltips/der_hullmod_incompatible.png";	
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("ballerinc_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("ballerinc_owner")); // Author


        tooltip.addImageWithText(PAD);
        //Also if you want to change shit also I suggest changing something here

        tooltip.addPara(getString("baller_desc"), pad, Color.ORANGE, Math.round(BigProblemo) + "%", Math.round(shieldophaso) + "%", Math.round(shieldophaso / 2f) + "%", Math.round(xivspiritsupport) + "%", Math.round(moarfluxtsussy) + "%", Math.round(gayso) + "%");


    
    }


}
