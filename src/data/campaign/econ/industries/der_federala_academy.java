package data.campaign.econ.industries;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.InstallableIndustryItemPlugin;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.econ.impl.GenericInstallableItemPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class der_federala_academy extends BaseIndustry {

	public static float DER_ORNIUMPRODUCTION_QUALITY = 0.50f;

	public static float DEFENSE_BONUS_STATIONARY_ORNIUM = 0.4f;
	public static float FLEET_BONUS_STATIONARY_ORNIUM = 0.75f;
	public boolean hasHeavy = true;
	public boolean hasPort = true;

	@Override
	public void apply() {
		super.apply(true);

		int size = market.getSize();

		int shipBonus = 0;
		float qualityBonus = DER_ORNIUMPRODUCTION_QUALITY;

		demand(Commodities.METALS, size + 2);
		demand(Commodities.RARE_METALS, size);
		demand(Commodities.ORE, size - 3);
		demand(Commodities.RARE_ORE, size - 4);
		demand(Commodities.HEAVY_MACHINERY, size - 1);
		demand(Commodities.HAND_WEAPONS, size - 2);
		demand(Commodities.CREW, size - 2);
		demand(Commodities.MARINES, size - 3);

		supply(Commodities.METALS, size - 3);
		supply(Commodities.RARE_METALS, size - 5);
		supply(Commodities.SUPPLIES, size - 1);
		supply(Commodities.HAND_WEAPONS, size - 3);
		supply(Commodities.HEAVY_MACHINERY, size - 4);
		supply(Commodities.SHIPS, size + 4);
		if (shipBonus > 0) {
			supply(1, Commodities.SHIPS, shipBonus, "Ornium Shipyard");
		}

		Pair<String, Integer> deficit = getMaxDeficit(Commodities.METALS, Commodities.RARE_METALS);
		int maxDeficit = size - 3; // to allow *some* production so economy doesn't get into an unrecoverable state
		if (deficit.two > maxDeficit) deficit.two = maxDeficit;

		applyDeficitToProduction(2, deficit,
				Commodities.SUPPLIES,
				Commodities.SHIPS,
				Commodities.ORE,
				Commodities.RARE_ORE,
				Commodities.METALS,
				Commodities.RARE_METALS,
				Commodities.MARINES,
				Commodities.CREW
		);

		float mult = getDeficitMult(Commodities.SUPPLIES);
		String extra = "";
		if (mult != 1) {
			String com = getMaxDeficit(Commodities.SUPPLIES).one;
			extra = " (" + getDeficitText(com).toLowerCase() + ")";
		}

		float defense_bonus = DEFENSE_BONUS_STATIONARY_ORNIUM;
		float bonus_fleet = FLEET_BONUS_STATIONARY_ORNIUM;
		market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).modifyMult(getModId(), 1f + defense_bonus * mult, getNameForModifier() + extra);
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyMult(getModId(), 1f + bonus_fleet * mult, getNameForModifier() + extra);
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).modifyFlat(getModId(), qualityBonus, "Ornium Style Production Shipyard");

		float stability = market.getPrevStability();
		if (stability < 5) {
			float stabilityMod = (stability - 5f) / 5f;
			stabilityMod *= 0.5f;
			market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).modifyFlat(getModId(), stabilityMod, getNameForModifier() + " - low stability");
		}

		if (!isFunctional()) {
			market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyFlat(getModId());
			supply.clear();
			unapply();
		}
	}

	@Override
	public void unapply() {
		super.unapply();

		market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyFlat(getModId());
		market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyMult(getModId());
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyFlat(getModId());
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyMult(getModId());
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyFlat(getModId(0));
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyMult(getModId(1));
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyMult(getModId(2));
		market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyMult(getModId(3));
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat(getModId());
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyMult(getModId());
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat(getModId(0));
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyMult(getModId(1));
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyMult(getModId(2));
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyMult(getModId(3));
	}

	@Override
	protected boolean canImproveToIncreaseProduction() {
		return true;
	}

	@Override
	protected void addPostSupplySection(TooltipMakerAPI tooltip, boolean hasSupply, IndustryTooltipMode mode) {
		super.addPostSupplySection(tooltip, hasSupply, mode);
	}

	@Override
	protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
		if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
			float total = DER_ORNIUMPRODUCTION_QUALITY;
			float def_bonus = DEFENSE_BONUS_STATIONARY_ORNIUM;
			float fleet_bonus = FLEET_BONUS_STATIONARY_ORNIUM;

			String totalDef = "+" + (int)Math.round(def_bonus * 100f) + "%";
			String totalFleet = "+" + (int)Math.round(fleet_bonus * 100f) + "%";
			String totalStr = "+" + (int)Math.round(total * 100f) + "%";
			Color h = Misc.getHighlightColor();
			Color h2 = Misc.getHighlightColor();
			Color h3 = Misc.getHighlightColor();
			float pad = 3f;
			float opad = 10f;
			if (def_bonus < 0) {
				h = Misc.getNegativeHighlightColor();
				totalDef = "" + (int)Math.round(def_bonus * 100f) + "%";
			}
			if (fleet_bonus < 0) {
				h2 = Misc.getNegativeHighlightColor();
				totalFleet = "" + (int)Math.round(fleet_bonus * 100f) + "%";
			}
			if (total < 0) {
				h3 = Misc.getNegativeHighlightColor();
				totalStr = "" + (int)Math.round(total * 100f) + "%";
			}

			if (def_bonus >= 0) tooltip.addPara("Ground Defenses: %s", opad, h, totalDef);
			if (fleet_bonus >= 0) tooltip.addPara("Fleet Size: %s", pad, h2, totalFleet);
			if (total >= 0) {
				tooltip.addPara("Ship quality: %s", pad, h3, totalStr);
				tooltip.addPara("*Quality bonus only applies for the largest ship producer in the faction.",
						Misc.getGrayColor(), opad);
			}
		}
	}


	@Override
	public boolean isAvailableToBuild() {
		return false;
	}

	@Override
	public boolean showWhenUnavailable() {
		return true;
	}

	public boolean isDemandLegal(CommodityOnMarketAPI com) {
		return true;
	}

	public boolean isSupplyLegal(CommodityOnMarketAPI com) {
		return true;
	}

	public float getPatherInterest() {
		float base = -500f;
		return base + super.getPatherInterest();
	}

}
