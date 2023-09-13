package data.scripts.utils;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;

import java.util.List;

public class dalton_utils extends BaseModPlugin {


    public static boolean playerHasCommodity(String item, int amount)
    {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null)
            return false;
        List<CargoStackAPI> playerCargoStacks = playerFleet.getCargo().getStacksCopy();

        for (CargoStackAPI cargoStack : playerCargoStacks) {
            if (cargoStack.isCommodityStack() && cargoStack.getCommodityId().equals(item) && cargoStack.getSize() > amount - 1) {
                return true;
            }
        }

        return false;
    }

    public static void removePlayerCommodity(String item, int amount)
    {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null)
            return;
        List<CargoStackAPI> playerCargoStacks = playerFleet.getCargo().getStacksCopy();

        for (CargoStackAPI cargoStack : playerCargoStacks) {
            if (cargoStack.isCommodityStack() && cargoStack.getCommodityId().equals(item)) {
                cargoStack.subtract(amount);
                if (cargoStack.getSize() <= 0)
                    playerFleet.getCargo().removeStack(cargoStack);
                return;
            }
        }
    }

    public static void addPlayerCommodity(String commodityId, int amount)
    {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null)
            return;
        CargoAPI playerFleetCargo = playerFleet.getCargo();
        playerFleetCargo.addCommodity(commodityId, amount);
    }


}

/*
    just gona keep it here for now


    public void advanceInCampaign(FleetMemberAPI member, float amount) {
        if(Global.getCurrentState() != GameState.TITLE) {
            Map<String, Object> data = Global.getSector().getPersistentData();
            if (member.getFleetData() != null && member.getFleetData().getFleet() != null && member.getFleetData().getFleet().equals(Global.getSector().getPlayerFleet())) {
                dalton_utils.removePlayerCommodity("COMMODITY", AMMOUNT);
            }
        }
    }


    public boolean canBeAddedOrRemovedNow(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        if(ship.getVariant().hasHullMod("HULLMOD")){
            return true;
        }else{
            return dalton_utils.playerHasCommodity("COMMODITY", AMMOUNT) && super.canBeAddedOrRemovedNow(ship, marketOrNull, mode);
        }
    }

    public String getCanNotBeInstalledNowReason(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        return !dalton_utils.playerHasCommodity("COMMODITY", AMMOUNT) ? "You do not have the required ammount of ITEM" : super.getCanNotBeInstalledNowReason(ship, marketOrNull, mode);
    }
 */
