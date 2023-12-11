package data.campaign.econ.industries.kmPMC;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.CampaignEventListener.FleetDespawnReason;
import com.fs.starfarer.api.campaign.FactionAPI.ShipPickMode;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.Industry.AICoreDescriptionMode;
import com.fs.starfarer.api.campaign.econ.Industry.ImprovementDescriptionMode;
import com.fs.starfarer.api.campaign.econ.Industry.IndustryTooltipMode;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase.PatrolFleetData;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.fleets.PatrolAssignmentAIV4;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory.PatrolType;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.OptionalFleetData;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteData;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteFleetSpawner;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteSegment;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD.RaidDangerLevel;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import data.scripts.dermond_ModPlugin;
import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

//Base code taken from SC fork from Corvus, for more info DM me or visit corvus
public class der_PMC_level1 extends BaseIndustry implements RouteFleetSpawner, FleetEventListener {
    protected IntervalUtil tracker = new IntervalUtil(Global.getSettings().getFloat("averagePatrolSpawnInterval") * 0.7F, Global.getSettings().getFloat("averagePatrolSpawnInterval") * 1.3F);
    protected float returningPatrolValue = 0.0F;

    //private static Logger log = Global.getLogger(dermond_ModPlugin.class);
    public static String ID = "der_PMC_level1";
    public static float DEFENSE_BONUS = 150.0F;
    public static float IMPROVE_DEFENSE_BONUS = 1.2F;
    public static final float ALPHA_CORE_BONUS = 0.25F;
    public static final float ALPHA_STAB_BONUS = 1.0F;
    public static final String FACTION = "dermond_KM_PMC";
    public static final float PATHER_INTEREST = -10.0F;
    public static final float PATHER_INTEREST_ALPHA = -15.0F;
    /*
   public boolean isHidden() {
      log.info("isHidden");
      if (this.market.getFactionId().equals("independent")) {
         log.info("false");
         return false;
      } else {
         log.info("true");
         RepLevel rep = this.market.getFaction().getRelationshipLevel("independent");
         return !rep.isPositive();
      }
   }

     */

    public void apply() {
        super.apply(true);
        int size = this.market.getSize();
        this.demand("supplies", size / 2 + 1);
        this.demand("fuel", size / 2 + 1);
        this.demand("ships", size / 2 + 1);
        MemoryAPI memory = this.market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, "$patrol", this.getModId(), true, -1.0F);
        if (!this.isFunctional()) {
            this.demand.clear();
            this.supply.clear();
            this.market.getStats().getDynamic().getMod("ground_defenses_mod").unmodifyFlat(ID);
            this.unapply();
            this.market.getStability().unmodifyFlat(ID);
        } else {
            this.market.getStats().getDynamic().getMod("ground_defenses_mod").modifyFlat(ID, DEFENSE_BONUS, this.getNameForModifier());
        }

    }

    public void unapply() {
        super.unapply();
        MemoryAPI memory = this.market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, "$patrol", this.getModId(), false, -1.0F);
        this.market.getStability().unmodifyFlat(this.getModId());
        this.market.getStats().getDynamic().getMod("ground_defenses_mod").unmodifyFlat(ID);
    }

    protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {
        return mode != IndustryTooltipMode.NORMAL || this.isFunctional();
    }

    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        if (mode != IndustryTooltipMode.NORMAL || this.isFunctional()) {
            float opad = 10.0F;
            Color h = Misc.getHighlightColor();
            tooltip.addPara("Pather intrigue: %s", 10.0F, h, new String[]{"-10"});
            tooltip.addPara("Ground defense strength: %s", 10.0F, h, new String[]{"+150"});
            this.addCustomDescriptions(tooltip, mode);
        }

    }

    public String getNameForModifier() {
        return this.getSpec().getName().contains("HQ") ? this.getSpec().getName() : Misc.ucFirst(this.getSpec().getName());
    }

    protected void addCustomDescriptions(TooltipMakerAPI tooltip, IndustryTooltipMode mode) {
        Color sephira_conclave = new Color(95, 95, 135, 255);
        tooltip.addPara("Adds a %s patrol around this colony.", 10.0F, sephira_conclave, new String[]{"Külai Merai"});
    }

    public boolean isDemandLegal(CommodityOnMarketAPI com) {
        return true;
    }

    public boolean isSupplyLegal(CommodityOnMarketAPI com) {
        return true;
    }

    protected void buildingFinished() {
        super.buildingFinished();
        this.tracker.forceIntervalElapsed();
    }

    protected void upgradeFinished(Industry previous) {
        super.upgradeFinished(previous);
        this.tracker.forceIntervalElapsed();
    }

    public void advance(float amount) {
        super.advance(amount);
        if (!Global.getSector().getEconomy().isSimMode()) {
            if (this.isFunctional()) {
                float days = Global.getSector().getClock().convertToDays(amount);
                float spawnRate = 1.0F;
                float rateMult = this.market.getStats().getDynamic().getStat("combat_fleet_spawn_rate_mult").getModifiedValue();
                spawnRate *= rateMult;
                float extraTime = 0.0F;
                if (this.returningPatrolValue > 0.0F) {
                    float interval = this.tracker.getIntervalDuration();
                    extraTime = interval * days;
                    this.returningPatrolValue -= days;
                    if (this.returningPatrolValue < 0.0F) {
                        this.returningPatrolValue = 0.0F;
                    }
                }

                this.tracker.advance(days * spawnRate + extraTime);
                if (this.tracker.intervalElapsed()) {
                    String sid = this.getRouteSourceId();
                    int light = this.getCount(PatrolType.FAST);
                    int medium = this.getCount(PatrolType.COMBAT);
                    int heavy = this.getCount(PatrolType.HEAVY);
                    boolean maxLight = true;
                    boolean maxMedium = true;
                    boolean maxHeavy = true;
                    WeightedRandomPicker<PatrolType> picker = new WeightedRandomPicker();
                    picker.add((PatrolType)PatrolType.HEAVY, (float)(1 - heavy));
                    picker.add((PatrolType)PatrolType.COMBAT, (float)(1 - medium));
                    picker.add((PatrolType)PatrolType.FAST, (float)(2 - light));
                    if (picker.isEmpty()) {
                        return;
                    }

                    PatrolType type = (PatrolType)picker.pick();
                    PatrolFleetData custom = new PatrolFleetData(type);
                    OptionalFleetData extra = new OptionalFleetData(this.market);
                    extra.fleetType = type.getFleetType();
                    RouteData route = RouteManager.getInstance().addRoute(sid, this.market, Misc.genRandomSeed(), extra, this, custom);
                    float patrolDays = 35.0F + (float)Math.random() * 10.0F;
                    route.addSegment(new RouteSegment(patrolDays, this.market.getPrimaryEntity()));
                }

            }
        }
    }

    public void reportAboutToBeDespawnedByRouteManager(RouteData route) {
    }

    public boolean shouldRepeat(RouteData route) {
        return false;
    }

    public int getCount(PatrolType... types) {
        int count = 0;
        Iterator var3 = RouteManager.getInstance().getRoutesForSource(this.getRouteSourceId()).iterator();

        while(true) {
            while(true) {
                RouteData data;
                do {
                    if (!var3.hasNext()) {
                        return count;
                    }

                    data = (RouteData)var3.next();
                } while(!(data.getCustom() instanceof PatrolFleetData));

                PatrolFleetData custom = (PatrolFleetData)data.getCustom();
                PatrolType[] var6 = types;
                int var7 = types.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    PatrolType type = var6[var8];
                    if (type == custom.type) {
                        ++count;
                        break;
                    }
                }
            }
        }
    }

    public int getMaxPatrols(PatrolType type) {
        if (type == PatrolType.FAST) {
            return (int)this.market.getStats().getDynamic().getMod("patrol_num_light_mod").computeEffective(0.0F);
        } else if (type == PatrolType.COMBAT) {
            return (int)this.market.getStats().getDynamic().getMod("patrol_num_medium_mod").computeEffective(0.0F);
        } else {
            return type == PatrolType.HEAVY ? (int)this.market.getStats().getDynamic().getMod("patrol_num_heavy_mod").computeEffective(0.0F) : 0;
        }
    }

    public boolean shouldCancelRouteAfterDelayCheck(RouteData route) {
        return false;
    }

    public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {
    }

    public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, FleetDespawnReason reason, Object param) {
        if (this.isFunctional()) {
            if (reason == FleetDespawnReason.REACHED_DESTINATION) {
                RouteData route = RouteManager.getInstance().getRoute(this.getRouteSourceId(), fleet);
                if (route.getCustom() instanceof PatrolFleetData) {
                    PatrolFleetData custom = (PatrolFleetData)route.getCustom();
                    if (custom.spawnFP > 0) {
                        float fraction = (float)(fleet.getFleetPoints() / custom.spawnFP);
                        this.returningPatrolValue += fraction;
                    }
                }
            }

        }
    }

    public CampaignFleetAPI spawnFleet(RouteData route) {
        if (!this.isFunctional()) {
            return null;
        } else {
            PatrolFleetData custom = (PatrolFleetData)route.getCustom();
            PatrolType type = custom.type;
            Random random = route.getRandom();
            float combat = 0.0F;
            float tanker = 0.0F;
            float freighter = 0.0F;
            String fleetType = type.getFleetType();
            switch(type) {
                case FAST:
                    combat = (float)Math.round(3.0F + random.nextFloat() * 2.0F) * 5.0F;
                    break;
                case COMBAT:
                    combat = (float)Math.round(6.0F + random.nextFloat() * 3.0F) * 5.0F;
                    tanker = (float)Math.round(random.nextFloat()) * 5.0F;
                    break;
                case HEAVY:
                    combat = (float)Math.round(10.0F + random.nextFloat() * 5.0F) * 5.0F;
                    tanker = (float)Math.round(random.nextFloat()) * 10.0F;
                    freighter = (float)Math.round(random.nextFloat()) * 10.0F;
            }

            FleetParamsV3 params = new FleetParamsV3(this.market, (Vector2f)null, "dermond_KM_PMC", route.getQualityOverride(), fleetType, combat, freighter, tanker, 0.0F, 0.0F, 0.0F, 0.0F);
            params.timestamp = route.getTimestamp();
            params.random = random;
            params.modeOverride = Misc.getShipPickMode(this.market);
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
            if (fleet != null && !fleet.isEmpty()) {
                fleet.setFaction(this.market.getFactionId(), true);
                fleet.setNoFactionInName(false);
                fleet.addEventListener(this);
                fleet.getMemoryWithoutUpdate().set("$isPatrol", true);
                if (type == PatrolType.FAST || type == PatrolType.COMBAT) {
                    fleet.getMemoryWithoutUpdate().set("$isCustomsInspector", true);
                }

                String postId = Ranks.POST_PATROL_COMMANDER;
                String rankId = Ranks.SPACE_COMMANDER;
                switch(type) {
                    case FAST:
                        rankId = Ranks.SPACE_LIEUTENANT;
                        break;
                    case COMBAT:
                        rankId = Ranks.SPACE_COMMANDER;
                        break;
                    case HEAVY:
                        rankId = Ranks.SPACE_CAPTAIN;
                }

                fleet.getCommander().setPostId(postId);
                fleet.getCommander().setRankId(rankId);
                this.market.getContainingLocation().addEntity(fleet);
                fleet.setFacing((float)Math.random() * 360.0F);
                fleet.setLocation(this.market.getPrimaryEntity().getLocation().x, this.market.getPrimaryEntity().getLocation().x);
                fleet.addScript(new PatrolAssignmentAIV4(fleet, route));
                if (custom.spawnFP <= 0) {
                    custom.spawnFP = fleet.getFleetPoints();
                }

                return fleet;
            } else {
                return null;
            }
        }
    }

    public String getRouteSourceId() {
        return this.getMarket().getId() + "_dermond_KM_PMC";
    }

    public boolean isAvailableToBuild() {
        boolean canBuild = false;
        Iterator var2 = this.market.getIndustries().iterator();

        while(var2.hasNext()) {
            Industry ind = (Industry)var2.next();
            if (ind != this && ind.isFunctional() && ind.getSpec().hasTag("spaceport")) {
                canBuild = true;
                break;
            }
        }

        if (this.market.getFactionId().equals("dermond_KM_PMC")) {
            return true;
        } else {
            RepLevel rep = this.market.getFaction().getRelationshipLevel("dermond_KM_PMC");
            return canBuild && rep.isPositive();
        }
    }

    public String getUnavailableReason() {
        return "Requires a functional spaceport";
    }

    public boolean showWhenUnavailable() {
        return true;
    }

    public String getCurrentImage() {
        return this.getSpecialItem() != null ? Global.getSettings().getSpriteName("industry", "dermond_KM_PMC") : super.getCurrentImage();
    }

    public float getPatherInterest() {
        if (this.isFunctional() && !this.isBuilding() && this.aiCoreId != null && this.aiCoreId.equals("alpha_core")) {
            return -15.0F;
        } else {
            return this.isFunctional() ? -10.0F : 0.0F;
        }
    }

    protected void applyAlphaCoreModifiers() {
        this.market.getStability().modifyFlat(this.getModId(1), 1.0F, "Alpha core (" + this.getNameForModifier() + ")");
    }

    protected void applyNoAICoreModifiers() {
        this.market.getStability().unmodifyFlat(this.getModId(1));
        this.demandReduction.unmodifyFlat(this.getModId(0));
    }

    protected void applyAlphaCoreSupplyAndDemandModifiers() {
        this.demandReduction.modifyFlat(this.getModId(0), (float)DEMAND_REDUCTION, "Alpha core");
    }

    protected void addAlphaCoreDescription(TooltipMakerAPI tooltip, AICoreDescriptionMode mode) {
        float opad = 10.0F;
        Color highlight = Misc.getHighlightColor();
        String pre = "Alpha-level AI core currently assigned. ";
        if (mode == AICoreDescriptionMode.MANAGE_CORE_DIALOG_LIST || mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            pre = "Alpha-level AI core. ";
        }

        if (mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            CommoditySpecAPI coreSpec = Global.getSettings().getCommoditySpec(this.aiCoreId);
            TooltipMakerAPI text = tooltip.beginImageWithText(coreSpec.getIconName(), 48.0F);
            text.addPara(pre + "Reduces upkeep cost by %s. Reduces demand by %s unit. Increases stability by %s. Reduces Pather intrigue by %s.", 0.0F, highlight, new String[]{(int)((1.0F - UPKEEP_MULT) * 100.0F) + "%", Misc.getRoundedValue(1.0F) + "", "" + DEMAND_REDUCTION, "-15"});
            tooltip.addImageWithText(10.0F);
        } else {
            tooltip.addPara(pre + "Reduces upkeep cost by %s. Reduces demand by %s unit. Increases stability by %s. Reduces Pather intrigue by %s.", 0.0F, highlight, new String[]{(int)((1.0F - UPKEEP_MULT) * 100.0F) + "%", "" + DEMAND_REDUCTION, "1", "-15"});
        }
    }

    public boolean canImprove() {
        return true;
    }

    protected void applyImproveModifiers() {
        if (this.isImproved()) {
            this.market.getStats().getDynamic().getMod("ground_defenses_mod").modifyMult("dermond_KM_level1_improve_bonus", IMPROVE_DEFENSE_BONUS, "Improvements (KM PMC)");
        } else {
            this.market.getStats().getDynamic().getMod("ground_defenses_mod").unmodifyMult("dermond_KM_level1_improve_bonus"); //
        }

    }

    public void addImproveDesc(TooltipMakerAPI info, ImprovementDescriptionMode mode) {
        float opad = 10.0F;
        Color highlight = Misc.getHighlightColor();
        float a = IMPROVE_DEFENSE_BONUS;
        String str = "×" + a + "";
        if (mode == ImprovementDescriptionMode.INDUSTRY_TOOLTIP) {
            info.addPara("Ground defenses increased by %s.", 0.0F, highlight, new String[]{str});
        } else {
            info.addPara("Increases ground defenses by %s.", 0.0F, highlight, new String[]{str});
        }

        info.addSpacer(10.0F);
        super.addImproveDesc(info, mode);
    }

    public RaidDangerLevel adjustCommodityDangerLevel(String commodityId, RaidDangerLevel level) {
        return level.next();
    }

    public RaidDangerLevel adjustItemDangerLevel(String itemId, String data, RaidDangerLevel level) {
        return level.next();
    }
}
