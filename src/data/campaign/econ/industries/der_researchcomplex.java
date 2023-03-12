package data.campaign.econ.industries;

import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;



public class der_researchcomplex extends BaseIndustry {

    public void apply() {
        super.apply(true);

        int size = market.getSize();

        demand(Commodities.SUPPLIES, size);
        demand(Commodities.MARINES, size + 1);
        demand(Commodities.HAND_WEAPONS, size - 2);

        float mult = getDeficitMult(Commodities.SUPPLIES, Commodities.MARINES, Commodities.HAND_WEAPONS);
        String extra = "";
        if (mult != 1) {
            String com = getMaxDeficit(Commodities.SUPPLIES, Commodities.MARINES, Commodities.HAND_WEAPONS).one;
            extra = " (" + getDeficitText(com).toLowerCase() + ")";
        }
    
    }


    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean canBeDisrupted() {
        return true;
    }

    @Override
    public boolean isAvailableToBuild() {
        return false;
    }

    @Override
    public boolean showWhenUnavailable() {
        return true;
    }

    @Override
	protected boolean canImproveToIncreaseProduction() {
		return false;
	}

    @Override
    public void unapply() {
        super.unapply();
    }

    public boolean isDemandLegal(CommodityOnMarketAPI com) {
		return true;
	}

}