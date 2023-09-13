package data.campaign.econ.industries;

import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Pair;

public class der_ordo_breeder extends BaseIndustry {

    @Override
    public void apply() {
        super.apply(true);

        float size = market.getSize();

        //float crew_present_on_market = market.getAllCommodities().;
        //float marines_present_on_market = market.;

        demand(Commodities.CREW, (int) (size * 0.3));
        demand(Commodities.MARINES, (int) (size * 0.5));
        demand(Commodities.ORGANS, (int) (size * 0.5));
        demand(Commodities.ORGANICS, (int) (size * 0.2));
        demand(Commodities.DRUGS, (int) (size * 0.4));


        supply(Commodities.CREW, (int) (size / 0.7));
        supply(Commodities.MARINES, (int) (size / 0.6));
        supply(Commodities.ORGANICS, (int) (size * 0.1));
        supply(Commodities.ORGANS, (int) (size * 0.3));



        Pair<String, Integer> deficit = getMaxDeficit(Commodities.METALS, Commodities.RARE_METALS);
        int maxDeficit = (int) (size) - 3; // to allow *some* production so economy doesn't get into an unrecoverable state
        if (deficit.two > maxDeficit) deficit.two = maxDeficit;

        applyDeficitToProduction(2, deficit,
                Commodities.CREW,
                Commodities.MARINES,
                Commodities.ORGANICS,
                Commodities.ORGANS,
                Commodities.DRUGS
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

}
