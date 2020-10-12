package ehnetwork.core.pet.repository;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import ehnetwork.core.pet.repository.token.PetChangeToken;
import ehnetwork.core.pet.repository.token.PetExtraToken;
import ehnetwork.core.pet.repository.token.PetSalesToken;
import ehnetwork.core.server.remotecall.AsyncJsonWebCall;
import ehnetwork.core.server.remotecall.JsonWebCall;

public class PetRepository
{
	private String _webAddress;
	
	public PetRepository(String webAddress)
	{
		_webAddress = webAddress;
	}

	public List<PetSalesToken> GetPets(List<PetSalesToken> petTokens)
	{
		return new JsonWebCall(_webAddress + "Pets/GetPets").Execute(new TypeToken<List<PetSalesToken>>(){}.getType(), petTokens);
	}

	public void AddPet(PetChangeToken token)
	{
		new AsyncJsonWebCall(_webAddress + "Pets/AddPet").Execute(token);
	}

	public void RemovePet(PetChangeToken token)
	{
		new AsyncJsonWebCall(_webAddress + "Pets/RemovePet").Execute(token);
	}

	public List<PetExtraToken> GetPetExtras(List<PetExtraToken> petExtraTokens)
	{
		return new JsonWebCall(_webAddress + "Pets/GetPetExtras").Execute(new TypeToken<List<PetExtraToken>>(){}.getType(), petExtraTokens);
	}

	public void UpdatePet(PetChangeToken token)
	{
		new AsyncJsonWebCall(_webAddress + "Pets/UpdatePet").Execute(token);
	}

	public void AddPetNameTag(String name)
	{
		new AsyncJsonWebCall(_webAddress + "Pets/AddPetNameTag").Execute(name);
	}

	public void RemovePetNameTag(String name)
	{
		new AsyncJsonWebCall(_webAddress + "Pets/RemovePetNameTag").Execute(name);
	}
}
