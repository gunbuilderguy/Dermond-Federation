package data.campaign.econ.conditions;

import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.net.IDN;
import java.util.Arrays;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.Arrays;

public class der_ecnomoical_crash extends BaseMarketConditionPlugin {
       /*
    private static String [] DER = new String [] {
            "dermond_federation",
    };

    public static float INCOME_BONUS = 0.25f;


    @Override
    public void apply(String id) {

        if (!market.getFactionId().contentEquals("dermond_federation")) {
            unapply(id);
            return;
        }

        market.getStats().getDynamic().getMod()

    }

    @Override
    public boolean showIcon() {
        return market.getFactionId().contentEquals("dermond_federation");
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getStability().unmodifyFlat(id);
        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodify(id);
        market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodify(id);
    }

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);

        if(market == null) {
            return;
        }

        tooltip.addPara(
                "%s stability bonus.",
                20f,
                Misc.getHighlightColor(),
                "+" + (int)STABILITY_BONUS
        );
        tooltip.addPara(
                "%s defense rating.",
                20f,
                Misc.getHighlightColor(),
                "+" + (int) (DEFENSE_BONUS_DER_WILLOFTHEPEOPLE * 100f) + "%");
        tooltip.addPara("%s fleet size",
                20f, Misc.getHighlightColor(),
                "+" + (int) ((FLEET_SIZE_DER_WILLOFTHEPEOPLE - 1f) * 100f) + "%");
    }

           */
}
