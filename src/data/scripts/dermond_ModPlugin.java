package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.world.DERGen;
import exerelin.campaign.SectorManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class dermond_ModPlugin extends BaseModPlugin {

    public static boolean isExerlin = false;

    private static void initDF() {
        if (isExerlin && !SectorManager.getCorvusMode()) {
            return;
        }
        new DERGen().generate(Global.getSector());
    }

    @Override
    public void onApplicationLoad() {
        isExerlin = Global.getSettings().getModManager().isModEnabled("nexerlin");
    }

    @Override
    public void onNewGame() {
        initDF();
    }
}