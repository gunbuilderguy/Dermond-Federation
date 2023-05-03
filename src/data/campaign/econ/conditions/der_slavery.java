package data.campaign.econ.conditions;

import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;


public class der_slavery extends BaseMarketConditionPlugin{
    @Override
    public boolean showIcon() {
        if (market.getFactionId().contentEquals("dermond_federation")) {
            return true;
        }
        return false;
    }
}
