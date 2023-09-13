package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

import java.util.Map;

public class der_holder extends BaseHullMod {

    @Override
    public void advanceInCampaign(FleetMemberAPI member, float amount) {

        /* how to do it for dummies

        step 1.
        boolean you shit

        boolean SHIT = false;


        step 2.
        make it so if hullmod present it wont run future scripts

        if (member.getVariant().hasHullMod("SHIT_HULLMOD")) {
            SHIT = true;
        }


        step 3.

        add it to the if ladder

        if (!SHIT && data.containsKey("SHIT_check_" + member.getId())) {
            data.remove("SHIT_check_" + member.getId());
            member.getVariant().getHullMods().remove("der_holder");
        }

         */

        boolean DTU = false;

        boolean DFF = false;

        boolean THM = false;

        boolean DEP = false;

        boolean DAM = false;

        Map<String, Object> data = Global.getSector().getPersistentData();

        if (member.getVariant().hasHullMod("DermondTargetingunit")) {
            DTU = true;
        } else if (member.getVariant().hasHullMod("DermondFertillaFlux")) {
            DFF = true;
        } else if (member.getVariant().hasHullMod("DermondTorhartaHangarmodification")) {
            THM = true;
        } else if (member.getVariant().hasHullMod("DermondEnginePower")) {
            DEP = true;
        } else if (member.getVariant().hasHullMod("DermondAltornoModifcation")) {
            DAM = true;
        }

        if (!DFF && data.containsKey("aifertilla_check_" + member.getId())) {
            data.remove("aifertilla_check_" + member.getId());
            member.getVariant().getHullMods().remove("der_holder");
        } else if (!DTU && (data.containsKey("aitergeting_dermond_check_" + member.getId()))) {
            data.remove("aitergeting_dermond_check_" + member.getId());
            member.getVariant().getHullMods().remove("der_holder");
        } else if (!THM && data.containsKey("aitorthata_check_" + member.getId())) {
            data.remove("aitorthata_check_" + member.getId());
            member.getVariant().getHullMods().remove("der_holder");
        } else if (!DEP && (data.containsKey("aiengine_check_" + member.getId()))) {
            data.remove("aiengine_check_" + member.getId());
            member.getVariant().getHullMods().remove("der_holder");
        } else if (!DAM && data.containsKey("aialtorno_check_" + member.getId())) {
            data.remove("aialtorno_check_" + member.getId());
            member.getVariant().getHullMods().remove("der_holder");
        }


        //aiengine_check_


    }
}
