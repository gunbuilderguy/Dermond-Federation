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

public class dermond_Aleri {
    

    public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Aleri");

        system.setBackgroundTextureFilename("graphics/backgrounds/background2.jpg");

        system.getLocation().set(31050, -29800); // On the outer Reaches of The Sector is our Rebels home.

        PlanetAPI Aleri_star = system.initStar("Jurija",
                "star_white",
                220f,
                450,
                2f,
                0.5f,
                1f);

        //system.setLightColor(new Color(255, 178, 102)); // system light color

        PlanetAPI Aleri1 = system.addPlanet("der_meria", Aleri_star, "Meria", "barren-bombarded", 124, 110, 3300, 180);
        Aleri1.setCustomDescriptionId("der_meria");

        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("Aleri_jump_1", "Merian Jump-point");
        jumpPoint1.setCircularOrbit(Aleri_star, 200, 3300, 180);
        jumpPoint1.setRelatedPlanet(Aleri1);
        jumpPoint1.setStandardWormholeToHyperspaceVisual();
        system.addEntity(jumpPoint1);

        system.addAsteroidBelt(Aleri1, 0, 300, 65, 200, 400);

        SectorEntityToken der_outpost_Alpha_1 = system.addCustomEntity("der_outpost_Alpha_1",
        "Outpost Alpha",
        "station_side04",
        "dermond_federation");
        der_outpost_Alpha_1.setCircularOrbitPointingDown(Aleri_star, 270, 4505, 200);
        der_outpost_Alpha_1.setCustomDescriptionId("der_outpost_Alpha_1");
        der_outpost_Alpha_1.setInteractionImage("illustrations", "orbital");


        system.addAsteroidBelt(Aleri_star, 490, 4500, 598, 687, 220); // Ring system located between inner and outer planets
        system.addRingBand(Aleri_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 4460, 200f, null, null);


        SectorEntityToken belnarhaBuoy = system.addCustomEntity(null, "Aleri Makeshift Nav Buoy", "nav_buoy_makeshift", "dermond_federation"); // Makeshift nav buoy at L5 of Orguk
        belnarhaBuoy.setCircularOrbit(Aleri_star, 310, 5500, 280);


        SectorEntityToken gatea = system.addCustomEntity("aleri_gate", // unique id
        "Gate of Despair", // name - if null, defaultName from custom_entities.json will be used
        "inactive_gate", // type of object, defined in custom_entities.json
        null); // faction
        gatea.setCircularOrbit(Aleri_star, 108, 6980, 365);

        PlanetAPI Aleri2 = system.addPlanet("der_tine", Aleri_star, "Tine", "barren-desert", 80, 66, 7345, 470);
        Aleri2.setCustomDescriptionId("der_tine");

        PlanetAPI Aleri3 = system.addPlanet("der_deni", Aleri_star, "Tine", "terran", 120, 96, 8357, 760);
        Aleri3.setCustomDescriptionId("der_dine");

        SectorEntityToken belnarhaRelay = system.addCustomEntity(null, "Aleri Makeshift Comm Relay", "comm_relay_makeshift", "luddic_path"); // Makeshift comm relay at L4 of Orguk
        belnarhaRelay.setCircularOrbit(Aleri_star, 190, 8950, 280);

        system.autogenerateHyperspaceJumpPoints(true, false);

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

