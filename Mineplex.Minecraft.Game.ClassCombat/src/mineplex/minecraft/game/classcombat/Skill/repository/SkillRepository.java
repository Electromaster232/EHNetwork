package mineplex.minecraft.game.classcombat.Skill.repository;

import java.util.List;

import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;

import mineplex.core.server.remotecall.JsonWebCall;
import mineplex.minecraft.game.classcombat.Skill.repository.token.SkillToken;

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
