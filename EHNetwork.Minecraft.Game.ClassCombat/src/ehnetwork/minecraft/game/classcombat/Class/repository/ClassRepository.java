package ehnetwork.minecraft.game.classcombat.Class.repository;

import java.util.List;

import com.google.gson.reflect.TypeToken;

import ehnetwork.core.server.remotecall.AsyncJsonWebCall;
import ehnetwork.core.server.remotecall.JsonWebCall;
import ehnetwork.minecraft.game.classcombat.Class.repository.token.ClassToken;
import ehnetwork.minecraft.game.classcombat.Class.repository.token.CustomBuildToken;

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
