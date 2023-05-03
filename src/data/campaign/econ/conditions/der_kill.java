package data.campaign.econ.conditions;


import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.net.IDN;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;

public class der_kill extends BaseMarketConditionPlugin{
    private static String [] DER = new String [] {
        "dermond_federation",
    };
    public static float DEFENSE_BONUS = 0.3f;
    public static float STABILITY_BONUS = 2f;
    public static float ACCESABILITY_BONUS = 0.3f;
    public static float INDUSTRY = -3f;
    public static float QUALITY = 0.5f;
    

    @Override
    public void apply(String id) {
        if (market.getFactionId().contentEquals("dermond_federation")) {
            return;
        } else {
            market.getAccessibilityMod().modifyFlat(getModId(), -ACCESABILITY_BONUS, "Will of Dermond");
            market.getStability().modifyFlat(getModId(), -STABILITY_BONUS, "Will of Dermond");
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).modifyMult(getModId(), DEFENSE_BONUS, "Will of Dermond");
            market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).modifyFlat(getModId(), -QUALITY, "Will of Dermond");

            //Taken from Unknown Skies, didn't ask and don't care  ¯\_(ツ)_/¯
            for(Industry i : market.getIndustries()){
                for(MutableCommodityQuantity c : i.getAllSupply()){
                    i.getSupply(c.getCommodityId()).getQuantity().modifyFlat(id, INDUSTRY - 2, "Will of Dermond");
                };
                for(MutableCommodityQuantity f : i.getAllDemand()){
                    i.getDemand(f.getCommodityId()).getQuantity().modifyFlat(id, -INDUSTRY, "Will of Dermond");
                }
            }
        }
    }

    @Override
    public boolean showIcon() {
        if (market.getFactionId().contentEquals("dermond_federation")) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
		market.getStability().unmodifyFlat(id);
        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodify(id);
        market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodify(id);
        market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat(id);

        //Taken from Unknown Skies, didn't ask and don't care  ¯\_(ツ)_/¯
        for(Industry i : market.getIndustries()){
            for(MutableCommodityQuantity c : i.getAllSupply()){
                i.getSupply(c.getCommodityId()).getQuantity().unmodify(id);
            };
            for(MutableCommodityQuantity f : i.getAllDemand()){
                i.getDemand(f.getCommodityId()).getQuantity().unmodify(id);
            }
        }
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
            "-" + (int)STABILITY_BONUS  
        );
        tooltip.addPara(
                "%s defense rating.",
                20f,
                Misc.getHighlightColor(),
                "" + (int) ((DEFENSE_BONUS - 1f) * 100f) + "%"
        );
        tooltip.addPara("%s accesability bonus.",
                20f, Misc.getHighlightColor(),
                "-" + (int) (ACCESABILITY_BONUS * 100f) + "%"
        );
        tooltip.addPara("%s ship quality bonus.",
                20f, Misc.getHighlightColor(),
                "-" + (int) (QUALITY * 100f) + "%"
        );

        tooltip.addPara("Supply is decreased by %s, and demand is increased by %s",
                20f, Misc.getHighlightColor(),
                "" + (int) ((INDUSTRY - 2) * -1f), "" + (int) ((INDUSTRY) * -1f)
        );
    }

}
