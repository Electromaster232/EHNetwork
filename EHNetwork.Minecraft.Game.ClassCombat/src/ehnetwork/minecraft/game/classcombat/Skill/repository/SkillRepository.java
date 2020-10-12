package ehnetwork.minecraft.game.classcombat.Skill.repository;

import java.util.List;

import com.google.gson.reflect.TypeToken;

import ehnetwork.core.server.remotecall.JsonWebCall;
import ehnetwork.minecraft.game.classcombat.Skill.repository.token.SkillToken;

public class SkillRepository
{
	private String _webAddress;
	
	public SkillRepository(String webAddress)
	{
		_webAddress = webAddress;
	}
	
	public List<SkillToken> GetSkills(List<SkillToken> skills)
	{
		return new JsonWebCall(_webAddress + "Dominate/GetSkills").Execute(new TypeToken<List<SkillToken>>(){}.getType(), skills);
	}
}
