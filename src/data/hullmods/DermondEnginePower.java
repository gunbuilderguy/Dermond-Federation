package data.hullmods;

//Some of these you don't need, but I will just keep them here just in case


import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

import com.fs.graphics.H;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.skills.EnergyWeaponMastery;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashSet;
import java.util.Set;

public class DermondEnginePower extends BaseHullMod {

    private String getString(String key) {
        return Global.getSettings().getString("der",  key);}

    //Positive
	private static Map SPEED = new HashMap();
	static {
	    SPEED.put(HullSize.FRIGATE, 10f);
		SPEED.put(HullSize.DESTROYER, 20f);
		SPEED.put(HullSize.CRUISER, 30f);
		SPEED.put(HullSize.CAPITAL_SHIP, 20f);
	}
	private static Map MANEUVARABILITY = new HashMap();
	static {
	    MANEUVARABILITY.put(HullSize.FRIGATE, 50f);
		MANEUVARABILITY.put(HullSize.DESTROYER, 40f);
		MANEUVARABILITY.put(HullSize.CRUISER, 35f);
		MANEUVARABILITY.put(HullSize.CAPITAL_SHIP, 25f);
	}


    //Negative
    public static final float SUPPLY_USE_MULT = 1.15f;


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
        stats.getMaxSpeed().modifyPercent(id, (float) SPEED.get(hullSize));
        stats.getTurnAcceleration().modifyPercent(id, (float) MANEUVARABILITY.get(hullSize));
        stats.getSuppliesPerMonth().modifyMult(id, SUPPLY_USE_MULT);
    }

    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if(ship == null)
			return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);
		String HullmodIncompatible = "graphics/icons/tooltips/der_hullmod_incompatible.png";				
        String CSTitle = "'Post-Collapse Dermondian Engieneering'";
        String DermondCrest = "graphics/factions/crest_Dermond_Federation.png";
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
        tooltip.addPara("%s " + getString("speed_increase"), pad, arr, SPEED.get(hullSize) + "%"  );
        tooltip.addPara("%s " + getString("maneuv_increase"), pad, arr, MANEUVARABILITY.get(hullSize) + "%"  );


        //Negative ones
        tooltip.addPara("%s " + getString("maintanence_increase"), pad, add, Math.round((SUPPLY_USE_MULT - 1f) * 100f) + "%");


        
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