package ehnetwork.minecraft.game.classcombat.item.repository;

import java.util.List;

import com.google.gson.reflect.TypeToken;

import ehnetwork.core.server.remotecall.JsonWebCall;

public class ItemRepository
{
	private String _webAddress;
	
	public ItemRepository(String webAddress)
	{
		_webAddress = webAddress;
	}
	
	public List<ItemToken> GetItems(List<ItemToken> items) 
	{
		return new JsonWebCall(_webAddress + "Dominate/GetItems").Execute(new TypeToken<List<ItemToken>>(){}.getType(), items);
	}
}
