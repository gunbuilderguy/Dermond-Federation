package data.hullmods.special;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import data.scripts.utils.seven_util_misc;

import java.util.Map;

public class DermondHunterAI_return extends BaseHullMod {

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {

        boolean hasAICoreHullmod = false;

        if (member.getVariant().hasHullMod("DermondHunterAI_Intergration")) {
            hasAICoreHullmod = true;
        }

        if (!hasAICoreHullmod) {
            Map<String, Object> data = Global.getSector().getPersistentData();
            if (data.containsKey("aiintHunter_check_" + member.getId())) {
                data.remove("aiintHunter_check_" + member.getId());
            }
            if(member.getFleetData().getFleet().equals(Global.getSector().getPlayerFleet())) {
                seven_util_misc.addPlayerCommodity("dermond_hunter_AI", 1);
            }
            member.getVariant().getHullMods().remove("DermondHunterAI_return"); //w/e the id of the hullmod is
        }
    }
}
