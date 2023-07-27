package data.scripts.world;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;

import java.io.ObjectInputStream.GetField;
import java.util.Objects;

import com.fs.starfarer.api.Global;
import data.scripts.world.systems.dermond_Bontora;
import data.scripts.world.systems.dermond_Aleri;

//I kinda hate coding, but if I need I will code...

/*

I hate this part of the mod

⠁⡼⠋⠀⣆⠀⠀⣰⣿⣫⣾⢿⣿⣿⠍⢠⠠⠀⠀⢀⠰⢾⣺⣻⣿⣿⣿⣷⡀⠀
⣥⠀⠀⠀⠁⠀⠠⢻⢬⠁⣠⣾⠛⠁⠀⠀⠀⠀⠀⠀⠀⠐⠱⠏⡉⠙⣿⣿⡇⠀
⢳⠀⢰⡖⠀⠀⠈⠀⣺⢰⣿⢻⣾⣶⣿⣿⣶⣶⣤⣤⣴⣾⣿⣷⣼⡆⢸⣿⣧⠀
⠈⠀⠜⠈⣀⣔⣦⢨⣿⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣅⣼⠛⢹⠀
⠀⠀⠀⠀⢋⡿⡿⣯⣭⡟⣟⣿⣿⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⡘⠀
⡀⠐⠀⠀⠀⣿⣯⡿⣿⣿⣿⣯⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠋⣉⢽⣿⡆⠀⠀
⢳⠀⠄⠀⢀⣿⣿⣿⣿⣿⣿⣿⠙⠉⠉⠉⠛⣻⢛⣿⠛⠃⠀⠐⠛⠻⣿⡇⠀⠀
⣾⠄⠀⠀⢸⣿⣿⡿⠟⠛⠁⢀⠀⢀⡄⣀⣠⣾⣿⣿⡠⣴⣎⣀⣠⣠⣿⡇⠀⠀
⣧⠀⣴⣄⣽⣿⣿⣿⣶⣶⣖⣶⣬⣾⣿⣾⣿⣿⣿⣿⣽⣿⣿⣿⣿⣿⣿⡇⠀⠀
⣿⣶⣈⡯⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠋⣹⢧⣿⣿⣿⣄⠙⢿⣿⣿⣿⠇⠀⠀
⠹⣿⣿⣧⢌⢽⣻⢿⣯⣿⣿⣿⣿⠟⣠⡘⠿⠟⠛⠛⠟⠛⣧⡈⠻⣾⣿⠀⠀⠀
⠀⠈⠉⣷⡿⣽⠶⡾⢿⣿⣿⣿⢃⣤⣿⣷⣤⣤⣄⣄⣠⣼⡿⢷⢀⣿⡏⠀⠀⠀
⠀⠀⢀⣿⣷⠌⣈⣏⣝⠽⡿⣷⣾⣏⣀⣉⣉⣀⣀⣀⣠⣠⣄⡸⣾⣿⠃⠀⠀⠀
⠀⣰⡿⣿⣧⡐⠄⠱⣿⣺⣽⢟⣿⣿⢿⣿⣍⠉⢀⣀⣐⣼⣯⡗⠟⡏⠀⠀⠀⠀
⣰⣿⠀⣿⣿⣴⡀⠂⠘⢹⣭⡂⡚⠿⢿⣿⣿⣿⡿⢿⢿⡿⠿⢁⣴⣿⣷⣶⣦⣤

 */

public class DERGen implements SectorGeneratorPlugin{
    
    public static void initFactionRelationships(SectorAPI sector) {    
        FactionAPI dermond = Global.getSector().getFaction("dermond_federation");

        for (FactionAPI faction : Global.getSector().getAllFactions()){
            //if (faction.getRelationship("dermond") > 0.2f) continue; //I guess if someone loves dermond that much, they can be above that???
            //if (faction.getRelationship("dermond") < 0f) continue; //I guess if someone hates dermond that much, they can be below that.
            if (Objects.equals(faction.getId(), "dermond_federation")) continue;
            dermond.setRelationship(faction.getId(), -0.2f); //Otherwise set all factions to -0.2.
        }

        //Vanilla factions
        dermond.setRelationship(Factions.LUDDIC_CHURCH, -0.3f);
        dermond.setRelationship(Factions.LUDDIC_PATH, 0.25f);
        dermond.setRelationship(Factions.TRITACHYON, -1f);
        dermond.setRelationship(Factions.PERSEAN, -0.7f);
        dermond.setRelationship(Factions.PIRATES, -1f);
        dermond.setRelationship(Factions.INDEPENDENT, 0.1f);
        dermond.setRelationship(Factions.DIKTAT, -0.8f);
        dermond.setRelationship(Factions.LIONS_GUARD, -1f);
        dermond.setRelationship(Factions.HEGEMONY, -0.8f);
        dermond.setRelationship(Factions.REMNANTS, -1f);
        dermond.setRelationship(Factions.PLAYER, -0.2f);
        dermond.setRelationship("armaarmatura", 0.3f); //Mech Good guy :+1: Tritach Bad guy :-1:
        dermond.setRelationship("adversary", -0.4f); //These guys have some connections to Dermond that's why :)
        dermond.setRelationship("sevencorp", 0.1f); //This is just because Seven and Dalton both helped me and Epta is a nice faction all in all.
        dermond.setRelationship("orks", 0.8f); //Bultach Good - Tritach Bad
        dermond.setRelationship("scalartech", -0.3f); //They bad :C
        dermond.setRelationship("sindrian_fuel", -0.3f); //They bad but have braindamage >:(
        dermond.setRelationship("diableavionics", -0.4f); //I still can't belive they use children as soldiers. Best part about them
        dermond.setRelationship("magellan_protectorate", -0.85f); //FUCK S0RE- oops, I wanted to say fuck monarchy? Maybe :>
        dermond.setRelationship("sylphon", 0f); //finish it pls
        dermond.setRelationship("blackrock_driveyards", -0.45f); //These guys litrally use The Mess in their systems!
        dermond.setRelationship("dassault_mikoyan", 0.15f); //I still love 6eme
        dermond.setRelationship("6eme_bureau", 0.35f); //These guys are good friends!
        dermond.setRelationship("blade_breakers", -0.45f); //These guys are cringe
        dermond.setRelationship("the_deserter", 0.1f); //These guys are cool
        dermond.setRelationship("brighton", 0.2f); //We will help refugees... But god dammit they are English!
        dermond.setRelationship("HMI", -0.3f); //lowtech paragon and astral smh smh. Oh and also Locomotev is Very cool!
        dermond.setRelationship("mess", -0.8f); //Bad me angy :C
        dermond.setRelationship("interstellarimperium", -1f); //I can't believe they use big nukes, not human driven nukes smh
        dermond.setRelationship("kadur_remnant", -0.2f); //F
        dermond.setRelationship("kadur_theocracy", 0.2f); //As you rise from the ashes, we will help you
        dermond.setRelationship("ORA", -0.25f); //These guys are french
        dermond.setRelationship("shadow_industry", -1f); //big tiddy doesn't fool me!
        dermond.setRelationship("ironshell", -1f); //Next time I will burn down all coffee fields so you won't have any!
        dermond.setRelationship("apex_design", 0.2f); //We love helping them!( **no :)** )
        dermond.setRelationship("star_federation", 0.05f); //We shall protect, and we shall help you.
        dermond.setRelationship("uaf", -1f); //If CY, or somebody on UAF server sees it, I hope you will now know that you can just increase maintanance cost up, but wait lore wise you are weak. And yes the only answer to UAF pilots is slavery and even better getting sent to "The Tree"! 
        dermond.setRelationship("tahlan_legioinfernalis", -1f); //Anime Tahl is the real leader of Legio, don't lie to me!
        dermond.setRelationship("SCY", 0.1f); //Just recently opened diplomatic ties, and I hope we guys can become good friends...
        dermond.setRelationship("MVS", 0.4f); //I will pay you 50 billion credits to fuck off from Sector Politics
        dermond.setRelationship("gmda", -0.6f); //Fail RP
        dermond.setRelationship("gmda_patrol", -0.6f); //Fail RP
        dermond.setRelationship("draco", -0.6f); //Fail RP
        dermond.setRelationship("fang", -0.6f); //Faril RP
        dermond.setRelationship("new_galactic_order", -1f); //Peace was never an option with these dumb fucks
        dermond.setRelationship("xhanempire", -1f); //These guys were tasked with building a superstructure near the tree, they must have some connection to it
        dermond.setRelationship("unitedpamed", 0.3f); //I kinda hate you ngl
        dermond.setRelationship("vic", -0.3f); //Astrat I want some catgirl portaits for science, can I have them? plz.
        dermond.setRelationship("yrxp", 0.35f); //I love Anime tbh
        dermond.setRelationship("aria", 0.1f); //These guys have very good ships
        dermond.setRelationship("ryaz", 0.65f); //These guys have good relations just because they have magic and Dermond needs it :)
        //dermond.setRelationship("", 0.0f); //faction id
    }

    @Override
    public void generate(SectorAPI sector) {
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("dermond_federation");
        
        initFactionRelationships(sector);

        new dermond_Bontora().generate(sector);
        new dermond_Aleri().generate(sector);
    }
}



//If one day I will get this mad...
/*    @Override
public void onNewGameAfterEconomyLoad() {
    OrkLoveSettings();

    ImportantPeopleAPI ip = Global.getSector().getImportantPeople();

    MarketAPI market1 = Global.getSector().getEconomy().getMarket("dregruk");
    if (market1 != null) {
        PersonAPI admin1 = Global.getFactory().createPerson();
        admin1.setId("orks_urlakk");
        admin1.setFaction("orks");
        admin1.setGender(FullName.Gender.MALE);
        admin1.setPostId(Ranks.POST_FACTION_LEADER);
        admin1.setRankId(Ranks.FACTION_LEADER);
        admin1.setImportance(PersonImportance.VERY_HIGH);
        admin1.getName().setFirst("Urlakk");
        admin1.getName().setLast("Urg");
        admin1.setPortraitSprite("graphics/portraits/ork_armor.png");
        admin1.getStats().setSkillLevel(Skills.INDUSTRIAL_PLANNING, 1);
        ip.addPerson(admin1);
        market1.setAdmin(admin1);
        market1.getCommDirectory().addPerson(admin1, 0);
        market1.addPerson(admin1);
    }

    MarketAPI market2 = Global.getSector().getEconomy().getMarket("orguk");
    if (market2 != null) {
        PersonAPI admin2 = Global.getFactory().createPerson();
        admin2.setId("orks_vorhgad");
        admin2.setFaction("orks");
        admin2.setGender(FullName.Gender.MALE);
        admin2.setPostId(Ranks.POST_FACTION_LEADER);
        admin2.setRankId(Ranks.FACTION_LEADER);
        admin2.setImportance(PersonImportance.VERY_HIGH);
        admin2.getName().setFirst("Vorhgad");
        admin2.getName().setLast("Bloodfang");
        admin2.setPortraitSprite("graphics/portraits/ork_captain.png");
        admin2.getStats().setSkillLevel(Skills.INDUSTRIAL_PLANNING, 1);
        ip.addPerson(admin2);
        market2.setAdmin(admin2);
        market2.getCommDirectory().addPerson(admin2, 0);
        market2.addPerson(admin2);
    }

    MarketAPI market3 = Global.getSector().getEconomy().getMarket("gathrog");
    if (market3 != null) {
        PersonAPI admin3 = Global.getFactory().createPerson();
        admin3.setId("orks_gharag");
        admin3.setFaction("orks");
        admin3.setGender(FullName.Gender.MALE);
        admin3.setPostId(Ranks.POST_FACTION_LEADER);
        admin3.setRankId(Ranks.FACTION_LEADER);
        admin3.setImportance(PersonImportance.VERY_HIGH);
        admin3.getName().setFirst("Gharag");
        admin3.getName().setLast("Bigtoof");
        admin3.setPortraitSprite("graphics/portraits/ork_mean.png");
        admin3.getStats().setSkillLevel(Skills.INDUSTRIAL_PLANNING, 1);
        ip.addPerson(admin3);
        market3.setAdmin(admin3);
        market3.getCommDirectory().addPerson(admin3, 0);
        market3.addPerson(admin3);
    }
}*/