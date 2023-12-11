package data.scripts.world.systems;

import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;

public class dermond_quasi {

    public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("quasi");

        system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");

        system.getLocation().set(15000, 15000);

        PlanetAPI quasi_star = system.initStar("quasi",
                "star_white",
                4000f,
                450,
                2f,
                0.5f,
                1f);

        system.autogenerateHyperspaceJumpPoints(true, true);



    }


}
