package data.scripts.utils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;

import java.util.List;

public class dalton_utils {


    public static boolean playerHasCommodity(String id)
    {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null)
            return false;
        List<CargoStackAPI> playerCargoStacks = playerFleet.getCargo().getStacksCopy();

        for (CargoStackAPI cargoStack : playerCargoStacks) {
            if (cargoStack.isCommodityStack() && cargoStack.getCommodityId().equals(id) && cargoStack.getSize() > 0) {
                return true;
            }
        }

        return false;
    }

    public static void removePlayerCommodity(String id)
    {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null)
            return;
        List<CargoStackAPI> playerCargoStacks = playerFleet.getCargo().getStacksCopy();

        for (CargoStackAPI cargoStack : playerCargoStacks) {
            if (cargoStack.isCommodityStack() && cargoStack.getCommodityId().equals(id)) {
                cargoStack.subtract(1);
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
