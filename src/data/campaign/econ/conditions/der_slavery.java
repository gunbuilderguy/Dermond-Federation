package data.campaign.econ.conditions;

import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;


public class der_slavery extends BaseMarketConditionPlugin{
    private static String [] DER = new String [] {
        "dermond_federation",
    };


    @Override
    public boolean showIcon() {
        if (market.getFactionId().contentEquals("dermond_federation")) {
            return true;
        }
        return false;
    }
}
