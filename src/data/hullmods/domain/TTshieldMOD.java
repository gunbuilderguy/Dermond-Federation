package data.hullmods.domain;


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
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;



public class TTshieldMOD extends BaseHullMod {
    private String getString(String key) {
        return Global.getSettings().getString("der", key);
    }

    public static Color SHIELD_COLOR = new Color(0.85f, 0.75f, 0.85f, 0.7f);

    //I am in paaain :>
    public static final float shield = 0.8f;
    public static final float arc = 1.15f;
    public static final float shield_flox = 0.8f;
    public static final float CR_degrade = 250f;
    public static final float supplies = 1.5f;


    //Stolen code >:)    

    public String getUnapplicableReason(ShipAPI ship) {
        if ((ship.getShield() == null) ||
                (ship.getVariant().hasHullMod("hmp_weaponsystemenergizer")) ||
                (ship.getVariant().hasHullMod("swp_shieldbypass")) ||
                (ship.getVariant().hasHullMod("hmp_crystalizedarmor")) ||
                (ship.getVariant().hasHullMod("hmp_traxymiumarmor")) ||
                (ship.getVariant().hasHullMod("tahlan_forcedoverdrive")))
            return "Ship Has No Shield";
        if ((ship.getVariant().hasHullMod("frontemitter")) ||
                (ship.getVariant().hasHullMod("frontshield")) ||
                (ship.getVariant().hasHullMod("advancedshieldemitter")) ||
                (ship.getVariant().hasHullMod("adaptiveshields")) ||
                (ship.getVariant().hasHullMod("stabilizedshieldemitter")) ||
                (ship.getVariant().hasHullMod("extendedshieldemitter")) ||
                (ship.getVariant().hasHullMod("hardenedshieldemitter")) ||
                (ship.getVariant().hasHullMod("SRD_prototype_nullpoint_shield")) ||
                (ship.getVariant().hasHullMod("hmp_submattershield")) ||
                (ship.getVariant().hasHullMod("hmp_deltashield")) ||
                (ship.getVariant().hasHullMod("brshields")))
            return "Incompatible With All Other Shield Mods";
        return null;
    }


    public boolean isApplicableToShip(ShipAPI ship) {
        return ship != null && (ship.getHullSpec().getDefenseType() == ShieldType.FRONT || ship.getHullSpec().getDefenseType() == ShieldType.OMNI) &&
                ((!ship.getVariant().getHullMods().contains("frontemitter")) &&
                        (!ship.getVariant().getHullMods().contains("frontshield")) &&
                        (!ship.getVariant().getHullMods().contains("advancedshieldemitter")) &&
                        (!ship.getVariant().getHullMods().contains("adaptiveshields")) &&
                        (!ship.getVariant().getHullMods().contains("stabilizedshieldemitter")) &&
                        (!ship.getVariant().getHullMods().contains("extendedshieldemitter")) &&
                        (!ship.getVariant().getHullMods().contains("hardenedshieldemitter")) &&
                        (!ship.getVariant().getHullMods().contains("swp_shieldbypass")) &&
                        (!ship.getVariant().getHullMods().contains("hmp_weaponsystemenergizer")) &&
                        (!ship.getVariant().getHullMods().contains("SRD_prototype_nullpoint_shield")) &&
                        (!ship.getVariant().getHullMods().contains("hmp_crystalizedarmor")) &&
                        (!ship.getVariant().getHullMods().contains("hmp_traxymiumarmor")) &&
                        (!ship.getVariant().getHullMods().contains("tahlan_forcedoverdrive")) &&
                        (!ship.getVariant().getHullMods().contains("hmp_submattershield")) &&
                        (!ship.getVariant().getHullMods().contains("brshields")) &&
                        (ship.getShield() != null));
    }

    //Actual stats
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getShieldDamageTakenMult().modifyMult(id, shield);
        stats.getShieldArcBonus().modifyMult(id, arc);
        stats.getShieldUpkeepMult().modifyMult(id, shield_flox);
        stats.getCRLossPerSecondPercent().modifyPercent(id, CR_degrade);
        stats.getSuppliesPerMonth().modifyMult(id, supplies);
    }

    
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) return;
        MutableShipStatsAPI stats = ship.getMutableStats();
        ShipAPI playerShip = Global.getCombatEngine().getPlayerShip();
        float HardFlux = ship.getFluxTracker().getHardFlux();
        float MaxFlux = ship.getFluxTracker().getMaxFlux();
        float HardFluxPercent = HardFlux / MaxFlux;

        stats.getShieldDamageTakenMult().modifyMult("shield", 0.75f - (0.50f * HardFluxPercent));
        stats.getShieldUpkeepMult().modifyMult("shield", 0f + (3f * HardFluxPercent));

        ship.getShield().setInnerColor(SHIELD_COLOR);
        ship.getShield().setRingColor(SHIELD_COLOR);
    }
    
    @Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) 
	{
        if (isForModSpec || ship == null) return;
        float HEIGHT = 64f;
        float PAD = 5f;
        Color YELLOW = new Color(241, 199, 0);			
        String CSTitle = "'Tri Tachyon Corporation Experimental Engieneering'";
        String OrdoCrest ="graphics/factions/crest_tritachyon.png" ;
		float pad = 2f;
		Color[] arr ={Misc.getPositiveHighlightColor(),Misc.getHighlightColor()};
        Color[] add ={Misc.getNegativeHighlightColor(),Misc.getHighlightColor()};		
        TooltipMakerAPI OrdoIcon = tooltip.beginImageWithText(OrdoCrest, HEIGHT);


        tooltip.addSectionHeading("Details", Alignment.MID, pad);

        
        OrdoIcon.addPara(CSTitle, pad, YELLOW, CSTitle );
        //This one actually spawns the  BIGtext.
        final Color flavor = new Color(110,110,110,255);
        OrdoIcon.addPara("%s", 6f, flavor, getString("TTshield_desc")); //Main text
        OrdoIcon.addPara("%s", 1f, flavor, getString("TTshield_guy")); // Author


        tooltip.addImageWithText(PAD);
        //Also if you want to change shit also I suggest changing something here

        //Positive bonuses
        
        tooltip.addPara("%s " + getString("shield_efficent"), pad, arr, Math.round((shield - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("shieldarc_more"), pad, arr, Math.round((arc - 1f) * 100f) + "%");
        tooltip.addPara("%s " + getString("shield_flux"), pad, arr, Math.round((shield_flox - 1f) * -100f) + "%");
        tooltip.addPara("%s " + getString("degradecr_fast"), pad, add, Math.round(CR_degrade) + "%");
        tooltip.addPara("%s " + getString("maintanence_increase"), pad, add, Math.round((supplies - 1f) * 100f) + "%");

    
    }

    //Bork

}