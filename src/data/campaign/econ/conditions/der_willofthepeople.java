package data.campaign.econ.conditions;

import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class der_willofthepeople extends BaseMarketConditionPlugin {

    public static float DEFENSE_BONUS_DF_WILLOFTHEPEOPLE = 2.0f;
    //public static float FLEET_SIZE_DF_WILLOFTHEPEOPLE = 0.8f;

    @Override
    public void apply(String id) {
        super.apply(id);

        if (!market.getFactionId().contentEquals("dermond_federation")) {
            unapply(id);
            return;
        }

        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).
                modifyMult(id, 1f + DEFENSE_BONUS_DF_WILLOFTHEPEOPLE, Misc.ucFirst(condition.getName().toLowerCase()));
/*       market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).
                modifyMult(id, 1f + FLEET_SIZE_DF_WILLOFTHEPEOPLE, Misc.ucFirst(condition.getName().toLowerCase()));*/
    }

    @Override
    public boolean showIcon() {
        return market.getFactionId().contentEquals("dermond_federation");
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);

        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodify(id);
        //market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodify(id);
    }

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);

        if(market == null) {
            return;
        }

        tooltip.addPara("%s ground defenses",
                10f, Misc.getHighlightColor(),
                "+" + (int) (DEFENSE_BONUS_DF_WILLOFTHEPEOPLE * 100f) + "%");

        /*tooltip.addPara("%s fleet size",
                10f, Misc.getHighlightColor(),
                "+" + (int) (FLEET_SIZE_DF_WILLOFTHEPEOPLE * 100f) + "%");*/
    }
}
