package data.campaign;

import java.util.Random;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.AICoreOfficerPlugin;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.FullName.Gender;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.BaseAICoreOfficerPluginImpl;
import com.fs.starfarer.api.impl.campaign.ids.Personalities;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import data.boring.derCommodities;


public class HunterAI extends BaseAICoreOfficerPluginImpl implements AICoreOfficerPlugin {
	
	
	public PersonAPI createPerson(String aiCoreId, String factionId, Random random) {
		if (random == null) random = new Random();
		
		PersonAPI person = Global.getFactory().createPerson();
		person.setFaction(factionId);
		person.setAICoreId(aiCoreId);
		
		CommoditySpecAPI spec = Global.getSettings().getCommoditySpec(aiCoreId);
		boolean HunterAI = derCommodities.HUNTER_AI.equals(aiCoreId);

		
		person.getStats().setSkipRefresh(true);
		
		person.setName(new FullName(spec.getName(), "", Gender.MALE));
		int points = 0;
		float mult = 1f;
		if (HunterAI) {
			person.setPortraitSprite("graphics/portraits/characters/HunterAI.png");
			person.getStats().setLevel(20);
			person.getStats().setSkillLevel(Skills.HELMSMANSHIP, 2);
			person.getStats().setSkillLevel(Skills.TARGET_ANALYSIS, 2);
			person.getStats().setSkillLevel(Skills.IMPACT_MITIGATION, 2);
			//person.getStats().setSkillLevel(Skills.SHIELD_MODULATION, 2);
			person.getStats().setSkillLevel(Skills.FIELD_MODULATION, 2);
			//person.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE, 2);
			person.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 2);
			//person.getStats().setSkillLevel(Skills.RELIABILITY_ENGINEERING, 2);
			person.getStats().setSkillLevel(Skills.COMBAT_ENDURANCE, 2);
			person.getStats().setSkillLevel(Skills.DAMAGE_CONTROL, 2);
			person.getStats().setSkillLevel(Skills.POINT_DEFENSE, 2);
			person.getStats().setSkillLevel(Skills.ENERGY_WEAPON_MASTERY, 2);
			person.getStats().setSkillLevel(Skills.OMEGA_ECM, 2);
		}
		if (points != 0) {
			person.getMemoryWithoutUpdate().set(AUTOMATED_POINTS_VALUE, points);
		}
		person.getMemoryWithoutUpdate().set(AUTOMATED_POINTS_MULT, mult);
		
		person.setPersonality(Personalities.RECKLESS);
		person.setRankId(Ranks.SPACE_CAPTAIN);
		person.setPostId(null);
		
		person.getStats().setSkipRefresh(false);
		
		return person;
	}
	
}