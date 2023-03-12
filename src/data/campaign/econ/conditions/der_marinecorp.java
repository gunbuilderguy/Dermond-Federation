package data.campaign.econ.conditions;


import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.econ.BaseHazardCondition;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.impl.campaign.procgen.ConditionGenDataSpec;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;


import java.util.Arrays;


public class der_marinecorp extends BaseHazardCondition {

    private static String [] DER = new String [] {
            "dermond_federation",
    };
    public static float DEFENSE_BONUS = 500;


    @Override
    public void apply(String id) {


        float mult = market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).getBonusMult();
            if (Arrays.asList(DER).contains(market.getFactionId())) {
                market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).modifyFlat(getModId(), DEFENSE_BONUS, "Dermond Marine Corp ");
            }
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);

            
        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyFlat(getModId());
        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodify(id);
    }


    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);



        tooltip.addPara(
                "%s defense rating.",
                20f,
                Misc.getHighlightColor(),
                "+" + (int)DEFENSE_BONUS
        );
    }
}

