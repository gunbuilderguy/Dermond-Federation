package data.campaign.econ.industries;

import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

import java.awt.*;

public class der_ordo_meat_grinder extends BaseIndustry {

    @Override
    public void apply() {
        super.apply(true);

        float size = market.getSize();

        //float crew_present_on_market = market.getAllCommodities().;
        //float marines_present_on_market = market.;

        demand(Commodities.CREW, (int) (size * 0.3));
        demand(Commodities.MARINES, (int) (size * 0.5));
        demand(Commodities.ORGANS, (int) (size * 0.5));


        supply(Commodities.ORGANICS, (int) (size * 0.7));
        supply(Commodities.ORGANS, (int) (size * 0.2));
        supply(Commodities.FOOD, (int) (size));
        supply(Commodities.DOMESTIC_GOODS, (int) (size * 0.2));
        supply(Commodities.LUXURY_GOODS, (int) (size * 0.2));
        supply(Commodities.DRUGS, (int) (size * 0.8));


        Pair<String, Integer> deficit = getMaxDeficit(Commodities.METALS, Commodities.RARE_METALS);
        int maxDeficit = (int) (size) - 3; // to allow *some* production so economy doesn't get into an unrecoverable state
        if (deficit.two > maxDeficit) deficit.two = maxDeficit;

        applyDeficitToProduction(2, deficit,
                Commodities.CREW,
                Commodities.MARINES,
                Commodities.ORGANS,
                Commodities.ORGANICS,
                Commodities.FOOD,
                Commodities.DOMESTIC_GOODS,
                Commodities.LUXURY_GOODS
        );

        float mult = getDeficitMult(Commodities.CREW);
        String extra = "";
        if (mult != 1) {
            String com = getMaxDeficit(Commodities.CREW).one;
            extra = " (" + getDeficitText(com).toLowerCase() + ")";
        }
    }

    @Override
    public void unapply() {
        super.unapply();
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
