package data.scripts.world.systems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator.SystemGenData;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import static com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator.addOrbitingEntities;

public class dermond_Bontora {

    public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Bontora");

        system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");

        system.getLocation().set(66500, -48650); // Far far away from the core, our rebels lie

        PlanetAPI belnarha_star = system.initStar("Belnärha",
                "star_orange",
                660f,
                450,
                5f,
                0.5f,
                2f);

        system.setLightColor(new Color(255, 178, 102)); // system light color

        PlanetAPI belnarha1 = system.addPlanet("der_altoh", belnarha_star, "Altoh", "barren-bombarded", 140, 110, 3300, 180);
        belnarha1.setCustomDescriptionId("der_altoh");

        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("bernaha_jump_1", "R Jump-point");
        jumpPoint1.setCircularOrbit(belnarha_star, 200, 3300, 180); // Angle at 200, L5 of Dreguk
        jumpPoint1.setRelatedPlanet(belnarha1);
        jumpPoint1.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint1);

        system.addAsteroidBelt(belnarha1, 125, 300, 65, 200, 600);

        system.addAsteroidBelt(belnarha_star, 490, 4500, 598, 687, 220); // Ring system located between inner and outer planets
        system.addRingBand(belnarha_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 4460, 200f, null, null);
        system.addRingBand(belnarha_star, "misc", "rings_dust0", 256f, 1, Color.red, 256f, 4540, 200f, null, null);

        PlanetAPI belnarha2 = system.addPlanet("der_bandore", belnarha_star, "Bandore", "terran", 250, 170, 6000, 280);
        belnarha2.setCustomDescriptionId("der_bandore");

        PlanetAPI belnarha3 = system.addPlanet("der_blockno", belnarha2, "Blockno", "tundra", 40, 50, 600, 29);
        belnarha3.setCustomDescriptionId("der_blockno");

        PlanetAPI belnarha4 = system.addPlanet("der_bronsta", belnarha_star, "Bronstra", "rocky_ice", 250, 165, 8500, 300);
        belnarha4.setCustomDescriptionId("der_bronsta");

        PlanetAPI belnarha5 = system.addPlanet("der_yodora", belnarha_star, "Yodora", "toxic_cold", 200, 120, 9200, 650);
        belnarha5.setCustomDescriptionId("der_yodora");

        SectorEntityToken gatea = system.addCustomEntity("bontora_gate", // unique id
        "Gate of Revolt", // name - if null, defaultName from custom_entities.json will be used
        "inactive_gate", // type of object, defined in custom_entities.json
        null); // faction
        gatea.setCircularOrbit(belnarha_star, 120, 6980, 365);


        SectorEntityToken belnarhaRelay = system.addCustomEntity(null, "Belnarha Comm Relay", "comm_relay", "dermond_federation"); // Makeshift comm relay at L4 of Orguk
        belnarhaRelay.setCircularOrbit(belnarha_star, 190, 6500, 280);

        SectorEntityToken belnarhaBuoy = system.addCustomEntity(null, "Belnarha Nav Buoy", "nav_buoy", "dermond_federation"); // Makeshift nav buoy at L5 of Orguk
        belnarhaBuoy.setCircularOrbit(belnarha_star, 310, 6500, 280);

        

        system.autogenerateHyperspaceJumpPoints(true, true);

        cleanup(system);
    }

    void cleanup(StarSystemAPI system) {
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}