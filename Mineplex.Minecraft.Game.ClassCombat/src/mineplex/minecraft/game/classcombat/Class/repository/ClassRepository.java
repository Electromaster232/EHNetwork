package mineplex.minecraft.game.classcombat.Class.repository;

import java.util.List;

import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;

import mineplex.core.server.remotecall.AsyncJsonWebCall;
import mineplex.core.server.remotecall.JsonWebCall;
import mineplex.minecraft.game.classcombat.Class.repository.token.ClassToken;
import mineplex.minecraft.game.classcombat.Class.repository.token.CustomBuildToken;

public class ClassRepository
{
	private String _webAddress;
	
	public ClassRepository(String webAddress)
	{
		_webAddress = webAddress;
	}

	public List<ClassToken> GetClasses(List<ClassToken> pvpClasses) 
	{
		return new JsonWebCall(_webAddress + "Dominate/GetClasses").Execute(new TypeToken<List<ClassToken>>(){}.getType(), pvpClasses);
	}
	
	public void SaveCustomBuild(CustomBuildToken token) 
	{
		new AsyncJsonWebCall(_webAddress + "PlayerAccount/SaveCustomBuild").Execute(token);
	}
}
